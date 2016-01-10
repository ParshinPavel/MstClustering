package pro.parshinpn;

import java.util.*;

/**
 * @author Паршин Павел.
 */
public class Graph {
    private Set<Vertex> vertices = new HashSet<>();

	/**
	 * Контейнер для хранения связей графа.
	 * В качестве ключа используется ID связи.
	 */
	private Map<Long, Edge> edges = new HashMap<>();

	/**
	 * Порядковый номер последней созданной вершины графа.
	 */
	private int lastOrderNumber;

	/**
	 * Суммарный вес рёбер в графе.
	 */
	private double totalWeight = 0;

    public boolean addVertex(Vertex v) {
	    if (vertices.contains(v)) {
		    return false;
	    } else {
		    v.setOrderNumber(lastOrderNumber);
		    ++lastOrderNumber;
		    return vertices.add(v);
	    }
    }

	public List<Vertex> getVertices() {
		return new ArrayList<>(vertices);
	}

	public List<Edge> getEdges() {
		return new ArrayList<>(edges.values());
	}

	public int getVerticesCount() {
		return vertices.size();
	}

	public int getEdgesCount() {
		return edges.size();
	}

	public double getTotalWeight() {
		return totalWeight;
	}

	public boolean containsEdge(long edgeId) {
		return edges.containsKey(edgeId);
	}

	public boolean containsEdge(Vertex sourceVertex, Vertex targetVertex) {
		long edgeId = Edge.id(sourceVertex, targetVertex);
		return containsEdge(edgeId);
	}

	public Edge getEdge(Vertex sourceVertex, Vertex targetVertex) {
		long edgeId = Edge.id(sourceVertex, targetVertex);
		return edges.get(edgeId);
	}

	public boolean connect(Edge e) {
		if (containsEdge(e.getId())) {
			return false;
		}

		e.getSourceVertex().addAdjacentVertex(e.getTargetVertex());
		e.getTargetVertex().addAdjacentVertex(e.getSourceVertex());
		edges.put(e.getId(), e);
		totalWeight += e.getWeight();
		return true;
	}

	public Edge connect(Vertex sourceVertex, Vertex targetVertex, double weight) {
		final long edgeId = Edge.id(sourceVertex, targetVertex);

		if (containsEdge(edgeId)) {
			return edges.get(edgeId);
		} else {
			sourceVertex.addAdjacentVertex(targetVertex);
			targetVertex.addAdjacentVertex(sourceVertex);

			Edge e = new Edge(sourceVertex, targetVertex, weight);
			edges.put(edgeId, e);
			totalWeight += e.getWeight();

			return e;
		}
	}

	public void disconnect(Vertex sourceVertex, Vertex targetVertex) {
		long edgeId = Edge.id(sourceVertex, targetVertex);
		if (containsEdge(edgeId)) {
			sourceVertex.removeAdjacentVertex(targetVertex);
			targetVertex.removeAdjacentVertex(sourceVertex);

			Edge e = edges.remove(edgeId);
			totalWeight -= e.getWeight();
		}
	}

	public void disconnect(Edge edge) {
		disconnect(edge.getSourceVertex(), edge.getTargetVertex());
	}

	public void resetVisitedVertex() {
		for (Vertex vertex : vertices) {
			vertex.setVisited(false);
		}
	}
}
