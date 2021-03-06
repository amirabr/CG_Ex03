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
	 * Calculate the intensity at the given point.
	 * 
	 * @param p - point to measure intensity at
	 * @return the color intensity at that point
	 */
	public abstract Vec getIntensityAtPoint(Point3D p);
	
	/**
	 * Getter for position.
	 * 
	 * @return position
	 */
	public abstract Point3D getPosition();
	
	/**
	 * Getter for direction.
	 * 
	 * @return direction
	 */
	public abstract Vec getDirection();
	
	/**
	 * Calculates the vector from pos to the light's position.
	 * 
	 * @param pos - position to measure from
	 * @return the vector from pos to this.position
	 */
	public abstract Vec vectorToMe(Point3D pos);
	
	/**
	 * Calculates the distance from pos the the light's position.
	 * 
	 * @param pos - position to measure from
	 * @return the distance from pos to this.position
	 */
	public abstract double distanceToMe(Point3D pos);
	
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
