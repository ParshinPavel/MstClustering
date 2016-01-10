package pro.parshinpn;

/**
 * @author Паршин Павел.
 */
public class Edge {
	private Vertex sourceVertex;
	private Vertex targetVertex;

	private double weight;

	/**
	 * Уникальный идентификатор связи.
	 */
	private long id;

	public Edge(Vertex sourceVertex, Vertex targetVertex, double weight) {
		if (sourceVertex == null) throw new IllegalArgumentException("Исходная вершина связи не может быть null!");
		if (targetVertex == null) throw new IllegalArgumentException("Конечная вершина связи не может быть null!");

		this.sourceVertex = sourceVertex;
		this.targetVertex = targetVertex;
		this.weight = weight;

		id = id(sourceVertex, targetVertex);
	}

	public Vertex getSourceVertex() {
		return sourceVertex;
	}

	public Vertex getTargetVertex() {
		return targetVertex;
	}

	public double getWeight() {
		return weight;
	}

	public long getId() {
		return id;
	}

	/**
	 * Вычисление уникального идентификатора связи
	 * для двух вершин. Вычисление не зависит от порядка
	 * аргументов, т.е. id(v1, v2) == id(v2, v1).
	 *
	 * @param v1 первая вершина.
	 * @param v2 вторая вершина.
	 *
	 * @return уникальный идентификатор связи.
	 */
	public static long id(Vertex v1, Vertex v2) {
		final int a = v1.getOrderNumber();
		final int b = v2.getOrderNumber();

		// Необходимо для реализации свойства коммутативности.
		long min = Math.min(a, b);
		// см. http://en.wikipedia.org/wiki/Cantor_pairing_function
		return (a + b)*(a + b + 1)/2 + min;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Edge edge = (Edge) o;

		if (id != edge.id) return false;
		if (!sourceVertex.equals(edge.sourceVertex)) return false;
		if (!targetVertex.equals(edge.targetVertex)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = sourceVertex.hashCode();
		result = 31 * result + targetVertex.hashCode();
		result = 31 * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public String toString() {
		final String sourceName = sourceVertex.getData().getName();
		final String targetName = targetVertex.getData().getName();
		return String.format("\"%s\" => \"%s\" (%f).", sourceName, targetName, weight);
	}
}
