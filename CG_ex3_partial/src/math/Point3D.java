package math;

public class Point3D extends Tuple {

	/**
	 * Default constructor, initializes point to (0, 0, 0).
	 */
	public Point3D() {
		super();
	}
	
	/**
	 * Constructor, initializes point to given coordinates.
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 */
	public Point3D(double x, double y, double z) {
		super(x, y, z);
	}

	/**
	 * Constructor, initializes point to given coordinates, in string representation.
	 * @param v - string representation of coordinates
	 */
	public Point3D(String p) {
		super(p);
	}
	
	/**
	 * Copy constructor, initializes point to given coordinates, from another point.
	 * @param p - other point
	 */
	public Point3D(Point3D p) {
		super(p);
	}
	
	/**
	 * Distance between this point and another point p.
	 * @param p - the other point
	 * @return the distance between the points
	 */
	public double distance(Point3D p) {
		double dx = Math.pow(this.x - p.x, 2);
		double dy = Math.pow(this.y - p.y, 2);
		double dz = Math.pow(this.z - p.z, 2);
		return Math.sqrt(dx + dy + dz);
	}
	
	public Vec vectorToAnotherPoint(Point3D p) {
		return new Vec(p.x - this.x, p.y - this.y, p.z - this.z);
	}
	
	/**
	 * Adds vector to point. Result is point.
	 * @param v - the vector to add
	 * @return the point at the end of the vector
	 */
	public Point3D addVector(Vec v) {
		return new Point3D(this.x + v.x, this.y + v.y, this.z + v.z);
	}
	
	/**
	 * Adds a vector to a point. Result is a point.
	 * @param p - the point
	 * @param v - the vector
	 * @return the point at the end of the vector
	 */
	public static Point3D addVectorToPoint(Point3D p, Vec v) {
		return p.addVector(v);
	}
	
	/**
	 * Distance between two points.
	 * @param p1 - the first point
	 * @param p2 - the second point
	 * @return the distance between the two points
	 */
	public static double distance(Point3D p1, Point3D p2) {
		return p1.distance(p2);
	}
	
	/**
	 * Returns the vector between two points.
	 * @param p1 - the first point
	 * @param p2 - the second point
	 * @return the vector between the two points
	 */
	public static Vec vectorBetweenTwoPoints(Point3D p1, Point3D p2) {
		return p1.vectorToAnotherPoint(p2);
	}
	
	/**
	 * Compares two points.
	 * @param p1 - the first point
	 * @param p2 - the second point
	 * @return true if the points have identical values, false otherwise
	 */
	public static boolean equals(Point3D p1, Point3D p2) {
		return p1.equals(p2);
	}

}

