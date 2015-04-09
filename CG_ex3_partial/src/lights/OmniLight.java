package lights;

import java.util.Map;
import java.util.Scanner;
import math.Point3D;
import math.Vec;

/**
 * Represents a omni-directional light source:
 * A light emitted from a single point in all directions
 *
 */
public class OmniLight extends Light {

	// Position of light in scene
	private Point3D position;
	
	// Attenuation
	private double kConst;
	private double kLinear;
	private double kQuadratic;
	
	/**
	 * Constructor.
	 * @param attributes - user attributes for OmniLight
	 */
	public OmniLight(Map<String, String> attributes) {
		
		// Initialize attributes from XML
		init(attributes);
		
	}
	
	/**
	 * Initialize attributes from XML.
	 * @param attributes - user attributes for OmniLight
	 */
	public void init(Map<String, String> attributes) throws IllegalArgumentException {
		
		// Initialize 'color' attribute
		// Default is (1, 1, 1)
		if (attributes.containsKey("color")) {
			color = new Vec(attributes.get("color"));
		} else {
			color = new Vec(1, 1, 1);
		}
		
		// Initialize 'position' attribute
		if (!attributes.containsKey("position")) {
			throw new IllegalArgumentException("Missing 'position' attribute");
		}
		position = new Point3D(attributes.get("position"));
		
		// Initialize 'attenuation' attribute
		// Default is (1, 0, 0)
		if (attributes.containsKey("attenuation")) {
			String attenuation = attributes.get("attenuation");
			Scanner s = new Scanner(attenuation);
			kConst 		= s.nextDouble();
			kLinear 	= s.nextDouble();
			kQuadratic 	= s.nextDouble();
			s.close();
		} else {
			kConst 		= 1;
			kLinear 	= 0;
			kQuadratic 	= 0;
		}
		
	}
	
	/**
	 * Calculate the intensity at the given point.
	 * @param p - the point
	 */
	public Vec getIntensityAtPoint(Point3D p) {
		
		// Calculate the distance between the light source and the object
		double d = Point3D.distance(p, position);
		
		// Calculate distance weakening factor 
		double weakening =  1 / (kConst + kLinear*d + kQuadratic*d*d);
		
		// Return the result
		return Vec.scale(weakening, color);
	
	}
	
	/**
	 * Getter for position
	 * @return position
	 */
	public Point3D getPosition() {
		return position;
	}
	
}
