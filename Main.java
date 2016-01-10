import pro.parshinpn.*;

import java.util.*;

public class Main {

	private static final Point[] POINTS = new Point[]{
			new Point("P1", 2.709080934524536, -1.0364959239959717, -1.9362084865570068),
			new Point("P2", 2.923645257949829, 1.5516517162322998, 1.1126995086669922),
			new Point("P3", 0.5295989513397217, 0.1415269374847412, 3.0490918159484863),
			new Point("P4", -1.4740893840789795, -3.516988515853882, 1.233896255493164),
			new Point("P5", -2.124799966812134, -4.8596062660217285, 0.3458544611930847),
			new Point("P6", -1.0827465057373047, -4.489010810852051, -0.8037613034248352),
			new Point("P7", -0.7635605335235596, -3.5328845977783203, 0.03742170333862305),
			new Point("P8", -1.1618667840957642, -3.6103110313415527, -0.4031369686126709),
			new Point("P9", -1.5830864906311035, -3.8527112007141113, 0.296390175819397),
			new Point("P10", -1.7283073663711548, -4.165205001831055, -0.2046224325895309),
	};

	private static final String NEW_LINE = System.getProperty("line.separator");

	public static void main(String[] args) {
		// Первичная инициализация графа и заполнение его данными.
		// Граф не является связным, поскольку все его вершины не соединены.
		final Graph graph = buildGraphFromPoints(POINTS);

		// Выполняем построение минимального остовного дерева
		// на основе массива точек в пространстве с помощью алгоритма Крускала.
		runKruskalMST(graph);

		// Вычисляем средний вес ребёр между вершинами построенного остовного дерева,
		// будем использовать данную величину в качестве
		// меры принадлежности вершины к кластеру.
		final double averageWeight = graph.getTotalWeight() / graph.getEdgesCount();
		System.out.println("Средний вес между вершинами минимального остовного дерева: " + averageWeight + NEW_LINE);

		System.out.println("=== Связи построенного минимального остовного дерева ===");
		for (Edge e : graph.getEdges()) {
			System.out.println(e);
		}
		System.out.println("========================================================" + NEW_LINE);

		final List<Cluster> clusters = doClustering(graph, averageWeight);
		System.out.println("Количество кластеров: " + clusters.size());

		if (clusters.isEmpty()) {
			return;
		}

		// Вывод всех найденных кластеров и поиск группы основных объектов 3D-сцены.

		int primaryClusterIndex = 0;
		int maxObjects = Integer.MIN_VALUE;
		for (int clusterIndex = 0; clusterIndex < clusters.size(); ++clusterIndex) {
			Cluster c = clusters.get(clusterIndex);
			System.out.println(String.format("=== Кластер #%d. Количество объектов: %d ===", clusterIndex + 1, c.size()));
			for (Point p : c) {
				System.out.println(p);
			}
			System.out.println("========================================================" + NEW_LINE);

			// @todo: необходимо рассмотреть случай, если два и более кластеров будут иметь одинаковое количество объектов.
			if (c.size() > maxObjects) {
				primaryClusterIndex = clusterIndex;
				maxObjects = c.size();
			}
		}

		System.out.println("Основная группа объектов представлена кластером #" + (primaryClusterIndex + 1));
		// Вычисляем положение pivot point.
		final Point pivot = findPivotPoint(clusters.get(primaryClusterIndex));
		System.out.println("Положение центра основной группы объектов: " + pivot);
	}

	private static void runKruskalMST(Graph graph) {
		final List<Vertex> vertices = graph.getVertices();
		final int verticesCount = vertices.size();

		// Построение множества всех возможных ребер для графа
		// (другими словами, соединяем каждую вершину друг с другом).
		final Set<Long> existEdges = new HashSet<>();
		final Queue<Edge> minEdgeQueue = new PriorityQueue<>(graph.getVerticesCount(), new Comparator<Edge>() {
			@Override
			public int compare(Edge o1, Edge o2) {
				return Double.compare(o1.getWeight(), o2.getWeight());
			}
		});

		for (int i = 0; i < verticesCount; ++i) {
			final Vertex currentVertex = vertices.get(i);

			for (int j = 0; j < verticesCount; ++j) {
				if (j == i) {
					continue;
				}

				final Vertex adjacentVertex = vertices.get(j);
				final long edgeId = Edge.id(currentVertex, adjacentVertex);
				if (!existEdges.contains(edgeId)) {
					final double edgeWeight = calcEuclideanDistance(currentVertex.getData(), adjacentVertex.getData());
					final Edge edge = new Edge(currentVertex, adjacentVertex, edgeWeight);
					existEdges.add(edgeId);
					minEdgeQueue.offer(edge);
				}
			}
		}

		// Используем union-find data type для того, чтобы
		// избежать замыкания дерева.
		final UnionFindStructure uf = new UnionFindStructure(verticesCount);

		// Выполняем построение MST пока есть доступные связи.
		// Минимальное остовное дерево считается построенным, если
		// количество связей равно количеству вершин в графе минус один.
		while (!minEdgeQueue.isEmpty() && graph.getEdgesCount() < graph.getVerticesCount() - 1) {
			final Edge e = minEdgeQueue.poll();
			final int p = e.getSourceVertex().getOrderNumber();
			final int q = e.getTargetVertex().getOrderNumber();
			if (!uf.connected(p, q)) {
				uf.union(p, q);
				graph.connect(e);
			}
		}
	}

	private static double calcEuclideanDistance(Point p, Point q) {
		final double dx = p.getX() - q.getX();
		final double dy = p.getY() - q.getY();
		final double dz = p.getZ() - q.getZ();

		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	private static List<Cluster> doClustering(Graph mst, double threshold) {
		// Проходимся по связям и разрываем те, вес которых
		// больше порогового значения.
		for (Edge e : mst.getEdges()) {
			if (e.getWeight() > threshold) {
				mst.disconnect(e);
				System.out.println("Разрываем связь: " + e);
			}
		}
		System.out.print(NEW_LINE);

		// Используем union-find data type для кластеризации.
		UnionFindStructure uf = new UnionFindStructure(mst.getVerticesCount());
		for (Edge e : mst.getEdges()) {
			final int p = e.getSourceVertex().getOrderNumber();
			final int q = e.getTargetVertex().getOrderNumber();
			uf.union(p, q);
		}

		// Теперь необходимо извлечь полученные кластеры и привести
		// их к требуемому виду.
		Map<Integer, Cluster> clusters = new HashMap<>(uf.count());
		for (Vertex v : mst.getVertices()) {
			final Integer clusterNumber = uf.find(v.getOrderNumber());
			if (clusters.containsKey(clusterNumber)) {
				clusters.get(clusterNumber).add(v.getData());
			} else {
				Cluster c = new Cluster();
				c.add(v.getData());
				clusters.put(clusterNumber, c);
			}
		}

		return new ArrayList<>(clusters.values());
	}

	private static Graph buildGraphFromPoints(Point[] points) {
		final Graph graph = new Graph();
		for (Point p : points) {
			Vertex v = new Vertex(p);
			graph.addVertex(v);
		}

		System.out.println("Количество вершин в графе: " + graph.getVerticesCount());

		return graph;
	}

	private static Point findPivotPoint(Cluster cluster) {
		double px = 0, py = 0, pz = 0;
		for (Point p : cluster) {
			px += p.getX();
			py += p.getY();
			pz += p.getZ();
		}
		px /= cluster.size();
		py /= cluster.size();
		pz /= cluster.size();

		return new Point("PivotPoint", px, py, pz);
	}
}
