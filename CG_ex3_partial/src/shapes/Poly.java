package shapes;

import java.util.Map;
import java.util.TreeMap;

import math.Point3D;
import math.Ray;
import math.Vec;

/**
 * Represents a convex polygon.
 * 
 * IMPORTANT NOTE
 * ==============
 * We assume the points are given in a counter-clockwise direction.
 * We assume that there are at least 3 points and 3 edges that are linearly independent.
 * 
 * TODO: make sure it's the case :)
 *
 */
public class Poly extends Surface {

	private Point3D[] p; 	// Array of points
	private int size; 		// Size of array
	
	/**
	 * Constructor.
	 */
	public Poly() {
		
	}
	
	/**
	 * Constructor.
	 * @param attributes - user attributes for Surface
	 */
	public Poly(Map<String, String> attributes) {
		
		// Initialize size to 0
		size = 0;
		
		// Initialize surface attributes
		commonInit(attributes);
		
		// Initialize shape attributes
		init(attributes);
		
	}
	
	/**
	 * Initialize attributes from XML.
	 * @param attributes - user attributes for Poly
	 */
	@Override
	public void init(Map<String, String> attributes) throws IllegalArgumentException {
	
		// First we sort the attributes, to make sure the points are in sorted order
		Map<String, String> treeMap = new TreeMap<String, String>(attributes);
		
		// Now for each point entry, parse it into our p array
		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			if (entry.getValue().startsWith("p")) {
				p[size++] = new Point3D(entry.getValue());
			}
		}
		
		// Sanity check
		if (size < 3) {
			throw new IllegalArgumentException("Invalid Poly");
		}
		
	}

	/**
	 * Get the normal to the surface at a specific point.
	 * The normal of each point on the poly's surface is
	 * calculated using the right-hand rule.
	 * @param p - the point
	 * @return the normal at that point
	 */
	@Override
	public Vec getNormalAtPoint(Point3D p) {
		
		// Calculate the vector p0->p1
		Vec a = Point3D.vectorBetweenTwoPoints(this.p[0], this.p[1]);
		
		// Calculate the vector p1->p2
		Vec b = Point3D.vectorBetweenTwoPoints(this.p[0], this.p[2]);
		
		// Calculate the cross product of the two
		Vec normal = Vec.crossProd(a, b);
		
		// Make sure we normalize it
		normal.normalize();
		
		// Return it
		return normal;
	}
	
	/**
	 * Check ray-poly intersection.
	 * @param ray - the ray
	 * @return the point of intersection, or null otherwise
	 */
	@Override
	public Point3D intersectsWith(Ray ray) {
		return null;
	}
	
	/**
	 * Getter for a specified point.
	 * @param i - the point number
	 * @return the point
	 */
	public Point3D getPoint(int i) {
		return p[i];
	}
	
	/**
	 * Getter for size.
	 * @return how many points the poly has
	 */
	public int getSize() {
		return size;
	}

}
