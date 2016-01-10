package pro.parshinpn;

/**
 * Абстрактное представление точки в 3D-пространстве.
 *
 * @author Паршин Павел.
 */
public class Point {
	private String name;

    private double x;
    private double y;
    private double z;

	public Point(String name, double x, double y, double z) {
		if (name == null) {
			throw new IllegalArgumentException("Имя вершины не может быть null!");
		}

		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Point)) return false;

		Point point = (Point) o;

		if (Double.compare(point.x, x) != 0) return false;
		if (Double.compare(point.y, y) != 0) return false;
		if (Double.compare(point.z, z) != 0) return false;
		return name.equals(point.name);

	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = name.hashCode();
		temp = Double.doubleToLongBits(x);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s(%f, %f, %f)", name, x, y, z);
	}
}
