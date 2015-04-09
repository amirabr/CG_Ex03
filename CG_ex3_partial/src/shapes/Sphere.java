package shapes;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

/**
 * Represents a sphere.
 *
 */
public class Sphere extends Surface {
	
	private Point3D center; 	// The center of the sphere
	private double radius;		// The radius of the sphere

	/**
	 * Constructor.
	 * @param attributes - user attributes for Sphere
	 */
	public Sphere(Map<String, String> attributes) {
		
		// Initialize surface attributes
		commonInit(attributes);
		
		// Initialize shape attributes
		init(attributes);
		
	}
	
	/**
	 * Initialize attributes from XML.
	 * @param attributes - user attributes for Sphere
	 */
	@Override
	public void init(Map<String, String> attributes) throws IllegalArgumentException {
		
		// Initialize 'center' attribute
		if (!attributes.containsKey("center")) {
			throw new IllegalArgumentException("Missing 'center' attribute");
		}
		center = new Point3D(attributes.get("center"));
		
		// Initialize 'radius' attribute
		if (!attributes.containsKey("radius")) {
			throw new IllegalArgumentException("Missing 'radius' attribute");
		}
		radius = Double.parseDouble(attributes.get("radius"));
		
	}

	/**
	 * Get the normal to the surface at a specific point.
	 * The normal of each point on the sphere's surface is
	 * the normalized subtraction of the point and the center.
	 * @param p - the point
	 * @return the normal at that point
	 */
	@Override
	public Vec getNormalAtPoint(Point3D p) {
		Vec normal = Point3D.vectorBetweenTwoPoints(center, p);
		normal.normalize();
		return normal;
	}
	
	/**
	 * Check ray-sphere intersection.
	 * If there are 2 intersections, return the closet one.
	 * @param ray - the ray
	 * @return the point of intersection, or null otherwise
	 */
	@Override
	public Point3D intersectsWith(Ray ray) {
		return null;
	}
	
	public Point3D getCenter() {
		return center;
	}
	
	public double getRadius() {
		return radius;
	}

}
