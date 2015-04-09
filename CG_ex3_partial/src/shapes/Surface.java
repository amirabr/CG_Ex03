package shapes;

import java.util.Map;

import math.Point3D;
import math.RGB;
import math.Ray;
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
	 * Common surface attributes, for all shapes.
	 * @param attributes - the surface attributes
	 */
	protected void commonInit(Map<String, String> attributes) {
		
		// Initialize 'mtl-diffuse' attribute
		// Default is (0.7, 0.7, 0.7)
		if (attributes.containsKey("mtl-diffuse")) {
			mtlDiffuse = new RGB(attributes.get("mtl-diffuse"));
		} else {
			mtlDiffuse = new RGB(0.7, 0.7, 0.7);
		}
		
		// Initialize 'mtl-specular' attribute
		// Default is (1, 1, 1)
		if (attributes.containsKey("mtl-specular")) {
			mtlSpecular = new RGB(attributes.get("mtl-specular"));
		} else {
			mtlSpecular = new RGB(1, 1, 1);
		}
		
		// Initialize 'mtl-ambient' attribute
		// Default is (0.1, 0.1, 0.1)
		if (attributes.containsKey("mtl-ambient")) {
			mtlAmbient = new RGB(attributes.get("mtl-ambient"));
		} else {
			mtlAmbient = new RGB(0.1, 0.1, 0.1);
		}
		
		// Initialize 'mtl-emission' attribute
		// Default is (0, 0, 0)
		if (attributes.containsKey("mtl-emission")) {
			mtlEmission = new RGB(attributes.get("mtl-emission"));
		} else {
			mtlEmission = new RGB(0, 0, 0);
		}
		
		// Initialize 'mtl-shininess' attribute
		// Default is 100
		if (attributes.containsKey("mtl-shininess")) {
			mtlShininess = Double.parseDouble(attributes.get("mtl-shininess"));
		} else {
			mtlShininess = 100;
		}
		
		// Initialize 'reflectance' attribute
		// Default is 0
		if (attributes.containsKey("reflectance")) {
			mtlShininess = Double.parseDouble(attributes.get("reflectance"));
		} else {
			mtlShininess = 0;
		}
		
	}
	
	/**
	 * Get the normal to the surface at a specific point.
	 * @param p - the point
	 * @return the normal at that point
	 */
	public abstract Vec getNormalAtPoint(Point3D p);
	
	/**
	 * Check ray-surface intersection.
	 * If there are 2 intersections, return the closet one.
	 * @param ray - the ray
	 * @return the point of intersection, or null otherwise
	 */
	public abstract Point3D intersectsWith(Ray ray);

}
