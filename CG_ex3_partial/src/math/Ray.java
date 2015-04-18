package math;

public class Ray {

	public Point3D p; 	// Point of origin
	public Vec v; 		// Ray direction
	
	/**
	 * Constructs a new ray
	 * @param p - point of origin
	 * @param v - ray direction
	 */
	public Ray(Point3D p, Vec v) {
		this.p = p;
		this.v = v;
		
		v.normalize();
	}
	
	public String toString() {
		return "p = " + p.toString() + "\nv = " + v.toString();
	}
	
}
