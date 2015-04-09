package lights;

import math.Point3D;
import math.Vec;
import ex3.render.raytrace.IInitable;

/**
 * Super class to represent a light source.
 *
 */
public abstract class Light implements IInitable {

	// Intensity of light
	protected Vec color;
	
	/**
	 * Calculate the intensity at a given point by this light source
	 * @param p - the point
	 * @return how much light gets to point p from this light source
	 */
	public abstract Vec getIntensityAtPoint(Point3D p);
	
	/**
	 * Is the name of the class specified of type Light?
	 * @param type - a string with a name of a class
	 * @return true if the class is a subclass of Light, false otherwise
	 */
	public static boolean isLight(String type) {
		type = type.toLowerCase();
		return type.equals("dir-light") || type.equals("omni-light") || type.equals("spot-light"); 
	}
	
}
