package pro.parshinpn;

import java.util.*;

/**
 * Абстрактное представление вершины графа.
 *
 * @author Паршин Павел.
 */
public class Vertex {
    private Point data;

	private boolean visited = false;

	private Set<Vertex> adjacentVertices = new HashSet<>();

	private int orderNumber = 0;

    public Vertex(Point data) {
        this.data = data;
    }

    public Point getData() {
        return data;
    }

	public void addAdjacentVertex(Vertex v) {
		adjacentVertices.add(v);
	}

	public void removeAdjacentVertex(Vertex v) {
		adjacentVertices.remove(v);
	}

	public List<Vertex> getAdjacentVertices() {
		return new ArrayList<>(adjacentVertices);
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;

        Vertex that = (Vertex) o;

        return data != null ? data.equals(that.data) : that.data == null;
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }
}
