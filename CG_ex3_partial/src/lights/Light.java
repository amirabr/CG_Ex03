package lights;

import math.Point3D;
import math.RGB;
import ex3.render.raytrace.IInitable;

/**
 * Super class to represent a light source.
 *
 */
public abstract class Light implements IInitable {

	protected RGB color; 	// Intensity
	
	/**
	 * Calculate the intensity at a given point by this light source
	 * @param p - the point
	 * @return how much light gets to point p from this light source
	 */
	public abstract RGB getIntensityAtPoint(Point3D p);
	
}
