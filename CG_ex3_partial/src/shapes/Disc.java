package shapes;

import java.util.Map;
import math.Point3D;
import math.Vec;

/**
 * Represents a disc.
 * a disc is a sphere with a normal to its plane.
 *
 */
public class Disc extends Sphere {

	private Vec normal; 	// Normal to disc plane
	
	/**
	 * Constructor.
	 * @param attributes - user attributes for Disc
	 */
	public Disc(Map<String, String> attributes) {
		
		// Initialize surface and shape attributes
		super(attributes);
		
		// Don't forget the normal!
		dontForget(attributes);
		
	}
	
	/**
	 * Get the 'normal' attribute from the XML
	 * @param attributes - user attributes for Disc
	 */
	private void dontForget(Map<String, String> attributes) {
		
		// Initialize 'normal' attribute
		if (!attributes.containsKey("normal")) {
			throw new IllegalArgumentException("Missing 'normal' attribute");
		}
		normal = new Vec(attributes.get("normal"));
		normal.normalize(); 	// Make sure our normal is normalized
		
	}
	
	/**
	 * Get the normal to the surface at a specific point.
	 * The normal of each point on the disc's surface is
	 * the same normal we got in init()!
	 * @param p - the point
	 * @return the normal at that point
	 */
	@Override
	public Vec getNormalAtPoint(Point3D p) {
		return normal;
	}

}
