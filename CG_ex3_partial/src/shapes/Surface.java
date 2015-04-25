package shapes;

import java.util.Map;
import math.Point3D;
import math.Vec;
import ex3.render.raytrace.IInitable;

/**
 * Super class to represent a surface.
 *
 */
public abstract class Surface implements IInitable {
	
	protected Vec mtlDiffuse; 		// Diffuse part of the flat material
	protected Vec mtlSpecular; 		// Specular part of the material
	protected Vec mtlAmbient; 		// Ambient part of the material
	protected Vec mtlEmission; 		// Emission part of the material
	protected double mtlShininess; 	// Power of the (V*R) in the formula (n)
	protected double reflectance; 	// Reflectance coefficient of the material
	
	/**
	 * Common surface attributes, for all shapes.
	 * 
	 * @param attributes - the surface attributes
	 */
	protected void commonInit(Map<String, String> attributes) {
		
		// Initialize 'mtl-diffuse' attribute
		// Default is (0.7, 0.7, 0.7)
		if (attributes.containsKey("mtl-diffuse")) {
			mtlDiffuse = new Vec(attributes.get("mtl-diffuse"));
		} else {
			mtlDiffuse = new Vec(0.7, 0.7, 0.7);
		}
		
		// Initialize 'mtl-specular' attribute
		// Default is (1, 1, 1)
		if (attributes.containsKey("mtl-specular")) {
			mtlSpecular = new Vec(attributes.get("mtl-specular"));
		} else {
			mtlSpecular = new Vec(1, 1, 1);
		}
		
		// Initialize 'mtl-ambient' attribute
		// Default is (0.1, 0.1, 0.1)
		if (attributes.containsKey("mtl-ambient")) {
			mtlAmbient = new Vec(attributes.get("mtl-ambient"));
		} else {
			mtlAmbient = new Vec(0.1, 0.1, 0.1);
		}
		
		// Initialize 'mtl-emission' attribute
		// Default is (0, 0, 0)
		if (attributes.containsKey("mtl-emission")) {
			mtlEmission = new Vec(attributes.get("mtl-emission"));
		} else {
			mtlEmission = new Vec(0, 0, 0);
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
			reflectance = Double.parseDouble(attributes.get("reflectance"));
		} else {
			reflectance = 0;
		}
		
	}
	
	/**
	 * Get the normal to the surface at a specific point.
	 * 
	 * @param p - the point to measure the normal at
	 * @return the normal at that point
	 */
	public abstract Vec getNormalAtPoint(Point3D p);
	
	/**
	 * Getter for emission.
	 * 
	 * @return mtlEmission
	 */
	public Vec getEmissionCoefficient() {
		return mtlEmission;
	}
	
	/**
	 * Getter for ambient.
	 * 
	 * @return mtlAmbient
	 */
	public Vec getAmbientCoefficient() {
		return mtlAmbient;
	}
	
	/**
	 * Getter for diffuse.
	 * 
	 * @return mtlDiffuse
	 */
	public Vec getDiffuseCoefficient() {
		return mtlDiffuse;
	}
	
	/**
	 * Getter for specular.
	 * 
	 * @return mtlSpecular
	 */
	public Vec getSpecularCoefficient() {
		return mtlSpecular;
	}
	
	/**
	 * Getter for shininess.
	 * 
	 * @return mtlShininess
	 */
	public double getShininessCoefficient() {
		return mtlShininess;
	}
	
	/**
	 * Getter for reflectance.
	 * 
	 * @return reflectance
	 */
	public double getReflectanceCoefficient() {
		return reflectance;
	}

}
