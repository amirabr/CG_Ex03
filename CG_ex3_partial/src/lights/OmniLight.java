package lights;

import java.util.Map;
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
	 * 
	 * @param attributes - user attributes for OmniLight
	 */
	public OmniLight(Map<String, String> attributes) {
		
		// Initialize attributes from XML
		init(attributes);
		
	}
	
	/**
	 * Initialize attributes from XML.
	 * 
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
		if (!attributes.containsKey("pos")) {
			throw new IllegalArgumentException("Missing 'pos' attribute");
		}
		position = new Point3D(attributes.get("pos"));
		
		// Initialize 'kc' attribute
		// Default is 1
		if (attributes.containsKey("kc")) {
			kConst = Double.parseDouble(attributes.get("kc"));
		} else {
			kConst = 1;
		}
		
		// Initialize 'kl' attribute
		// Default is 0
		if (attributes.containsKey("kl")) {
			kLinear = Double.parseDouble(attributes.get("kl"));
		} else {
			kLinear = 0;
		}
		
		// Initialize 'kq' attribute
		// Default is 0
		if (attributes.containsKey("kq")) {
			kQuadratic = Double.parseDouble(attributes.get("kq"));
		} else {
			kQuadratic = 0;
		}
		
	}
	
	/**
	 * Calculate the intensity at the given point.
	 * 
	 * @param p - point to measure intensity at
	 * @return the color intensity at that point
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
	 * Getter for position.
	 * 
	 * @return position
	 */
	public Point3D getPosition() {
		return position;
	}
	
	/**
	 * Getter for direction.
	 * 
	 * @return direction
	 */
	public Vec getDirection() {
		return null;
	}
	
	/**
	 * Calculates the vector from pos to the light's position.
	 * 
	 * @param pos - position to measure from
	 * @return the vector from pos to this.position
	 */
	public Vec vectorToMe(Point3D pos) {
		return Point3D.vectorBetweenTwoPoints(pos, position);
	}
	
	/**
	 * Calculates the distance from pos the the light's position.
	 * 
	 * @param pos - position to measure from
	 * @return the distance from pos to this.position
	 */
	public double distanceToMe(Point3D pos) {
		return Point3D.distance(pos, position);
	}
	
}
