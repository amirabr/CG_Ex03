package lights;

import java.util.Map;
import java.util.Scanner;
import math.Point3D;
import math.RGB;
import math.Vec;

/**
 * Represents a spot light source:
 * A light emitted from a single point in a specific direction
 *
 */
public class SpotLight extends Light {

	// Position of light in scene
	private Point3D position;
	
	// Direction of light in scene
	private Vec direction;
	
	// Attenuation
	private double kConst;
	private double kLinear;
	private double kQuadratic;
	
	/**
	 * Constructor.
	 * @param attributes - user attributes for SpotLight
	 */
	public SpotLight(Map<String, String> attributes) {
		
		// Initialize attributes from XML
		init(attributes);
		
	}
	
	/**
	 * Initialize attributes from XML.
	 * @param attributes - user attributes for SpotLight
	 */
	public void init(Map<String, String> attributes) throws IllegalArgumentException {
		
		// Initialize 'color' attribute
		// Default is (1, 1, 1)
		if (attributes.containsKey("color")) {
			color = new RGB(attributes.get("color"));
		} else {
			color = new RGB(1, 1, 1);
		}
		
		// Initialize 'position' attribute
		if (!attributes.containsKey("position")) {
			throw new IllegalArgumentException("Missing 'position' attribute");
		}
		position = new Point3D(attributes.get("position"));
		
		// Initialize 'direction' attribute
		if (!attributes.containsKey("direction")) {
			throw new IllegalArgumentException("Missing 'direction' attribute");
		}
		direction = new Vec(attributes.get("direction"));
		
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
	public RGB getIntensityAtPoint(Point3D p) {
		
		// Calculate the distance between the light source and the object
		double d = Point3D.distance(p, position);
		
		// Calculate the vector between the light source and the object
		Vec L = Point3D.vectorBetweenTwoPoints(position, p);
		
		// Calculate distance and angle weakening factor 
		double weakening = Vec.dotProd(direction, L) / (kConst + kLinear*d + kQuadratic*d*d);
		
		// Return the result
		return RGB.scale(weakening, color);
	
	}
	
}
