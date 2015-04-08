package math;

/**
 * Super class to represent a 3-tuple.
 * To be inherited by Vec, Point3D and similar.
 * @author amir
 *
 */
public class Tuple {
	
	public double x, y, z;
	
	/**
	 * Default constructor, initializes tuple to (0, 0, 0). 
	 */
	public Tuple() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * Constructor, initializes tuple to given coordinates.
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 */
	public Tuple(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Copy constructor, initializes tuple to given coordinates, from another tuple.
	 * @param t - other tuple
	 */
	public Tuple(Tuple t) {
		this.x = t.x;
		this.y = t.y;
		this.z = t.z;
	}
	
	/**
	 * Compares tuple to another tuple.
	 * @param t - other tuple
	 * @return true if they have the same coordinates, false otherwise 
	 */
	public boolean equals(Tuple t) {
		return ((this.x == t.x) && (this.y == t.y) && (this.z == t.z));
	}
	
	/**
	 * Returns a string representation of the tuple, in (x, y, z) format.
	 */
	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	/**
	 * Returns new tuple instance with identical coordinates.
	 */
	@Override
	public Tuple clone() {
		return new Tuple(this);
	}

}
