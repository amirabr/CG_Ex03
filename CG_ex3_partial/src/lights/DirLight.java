package lights;

import java.util.Map;
import math.Point3D;
import math.Vec;

/**
 * Represents a directional light source:
 * A light emitted from infinity with parallel rays.
 *
 */
public class DirLight extends Light {

	// Direction of light in scene
	private Vec direction;
	
	/**
	 * Constructor.
	 * 
	 * @param attributes - user attributes for DirLight
	 */
	public DirLight(Map<String, String> attributes) {
		
		// Initialize attributes from XML
		init(attributes);
		
	}
	
	/**
	 * Initialize attributes from XML.
	 * 
	 * @param attributes - user attributes for DirLight
	 */
	public void init(Map<String, String> attributes) throws IllegalArgumentException {
		
		// Initialize 'color' attribute
		// Default is (1, 1, 1)
		if (attributes.containsKey("color")) {
			color = new Vec(attributes.get("color"));
		} else {
			color = new Vec(1, 1, 1);
		}
		
		// Initialize 'direction' attribute
		if (!attributes.containsKey("direction")) {
			throw new IllegalArgumentException("Missing 'direction' attribute");
		}
		direction = new Vec(attributes.get("direction"));
		
	}
	
	/**
	 * Calculate the intensity at the given point.
	 * In this case, the amount of light is the same at every point,
	 * so no calculation is needed.
	 * 
	 * @param p - point to measure intensity at
	 * @return the color intensity at that point
	 */
	public Vec getIntensityAtPoint(Point3D p) {
		return color;
	}
	
	/**
	 * Getter for position.
	 * 
	 * @return null
	 */
	public Point3D getPosition() {
		return null;
	}
	
	/**
	 * Getter for direction.
	 * 
	 * @return direction
	 */
	public Vec getDirection() {
		return direction;
	}
	
	/**
	 * Calculates the vector from pos to the light's position.
	 * In this case, there is no position, so just flip the
	 * direction vector of the light source.
	 * 
	 * @param pos - position to measure from
	 * @return the vector from pos to this.position
	 */
	public Vec vectorToMe(Point3D pos) {
		return Vec.negate(direction);
	}
	
	/**
	 * Calculates the distance from pos the the light's position.
	 * In this case, there is no position, so just return infinity.
	 * 
	 * @param pos - position to measure from
	 * @return infinity
	 */
	public double distanceToMe(Point3D pos) {
		return Double.POSITIVE_INFINITY;
	}
	
}
