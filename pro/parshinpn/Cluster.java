package pro.parshinpn;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Паршин Павел.
 */
public class Cluster implements Iterable<Point> {
	private Set<Point> points = new HashSet<>();

	public boolean add(Point p) {
		return points.add(p);
	}

	public int size() {
		return points.size();
	}

	@Override
	public Iterator<Point> iterator() {
		return points.iterator();
	}
}
