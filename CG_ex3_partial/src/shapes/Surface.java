package shapes;

import math.Point3D;
import math.RGB;
import math.Vec;
import ex3.render.raytrace.IInitable;

/**
 * Super class to represent a surface.
 *
 */
public abstract class Surface implements IInitable {
	
	protected RGB mtlDiffuse; 		// Diffuse part of the flat material
	protected RGB mtlSpecular; 		// Specular part of the material
	protected RGB mtlAmbient; 		// Ambient part of the material
	protected RGB mtlEmission; 		// Emission part of the material
	protected double mtlShininess; 	// Power of the (𝑉*𝑅) in the formula (𝑛)
	protected double reflectance; 	// Reflectance coefficient of the material
	
	/**
	 * Get the normal to the surface at a specific point.
	 * @param p - the point
	 * @return the normal at that point
	 */
	public abstract Vec getNormalAtPoint(Point3D p);

}
