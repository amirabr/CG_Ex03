package lights;

import java.util.Map;
import math.Point3D;
import math.RGB;
import math.Vec;

/**
 * Represents a directional light source:
 * A light emitted from infinity with parallel rays.
 *
 */
public class DirLight extends Light {

	private Vec direction;
	
	/**
	 * Constructor.
	 * @param attributes - user attributes for camera
	 */
	public DirLight(Map<String, String> attributes) {
		
		// Initialize attributes from XML
		init(attributes);
		
	}
	
	/**
	 * Initialize attributes from XML.
	 * @param attributes - user attributes for camera
	 */
	public void init(Map<String, String> attributes) throws IllegalArgumentException {
		
		// Initialize 'color' attribute
		if (!attributes.containsKey("color")) {
			throw new IllegalArgumentException("Missing 'color' attribute");
		}
		color = new RGB(attributes.get("color"));
		
		// Initialize 'direction' attribute
		if (!attributes.containsKey("direction")) {
			throw new IllegalArgumentException("Missing 'direction' attribute");
		}
		direction = new Vec(attributes.get("direction"));
		
	}
	
	/**
	 * Calculate the intensity at the given point.
	 * Note: in this case, there is no point
	 */
	public RGB getIntensityAtPoint(Point3D p) {
		return color;
	}
	
}
