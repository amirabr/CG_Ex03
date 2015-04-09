package ex3.render.raytrace;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import shapes.Disc;
import shapes.Intersection;
import shapes.Poly;
import shapes.Sphere;
import shapes.Surface;
import lights.DirLight;
import lights.Light;
import lights.OmniLight;
import lights.SpotLight;
import math.Point3D;
import math.Ray;
import math.Vec;

/**
 * A Scene class containing all the scene objects including camera, lights and surfaces.
 * 
 */
public class Scene implements IInitable {

	private Vec bgColor; 				// Background color of the scene
	private String bgTexture; 			// Background image of the scene
	private int maxRecLvl; 				// Max number of recursive rays when calculating reflections
	private Vec ambientLight; 			// Ambient light of the scene
	
	protected List<Surface> surfaces; 	// All of the surfaces in the scene
	protected List<Light> lights; 	 	// All of the lights in the scene
	protected Camera camera; 			// The camera of the scene

	/**
	 * Constructor.
	 */
	public Scene() {

		surfaces = new LinkedList<Surface>(); 	// No surfaces
		lights = new LinkedList<Light>(); 		// No lights
		camera = new Camera(); 					// Empty camera
		
	}

	public void init(Map<String, String> attributes) {
	
		// Initialize 'background-col' attribute
		// Default is (0, 0, 0)
		if (attributes.containsKey("background-col")) {
			bgColor = new Vec(attributes.get("background-col"));
		} else {
			bgColor = new Vec(0, 0, 0);
		}
		
		// Initialize 'background-tex' attribute
		// Default is null
		if (attributes.containsKey("background-tex")) {
			bgTexture = attributes.get("background-tex");
		} else {
			bgTexture = null;
		}
		
		// Initialize 'max-recursion-level' attribute
		// Default is 10
		if (attributes.containsKey("max-recursion-level")) {
			maxRecLvl = Integer.parseInt(attributes.get("max-recursion-level"));
		} else {
			maxRecLvl = 10;
		}
		
		// Initialize 'ambient-light' attribute
		// Default is (0, 0, 0)
		if (attributes.containsKey("ambient-light")) {
			ambientLight = new Vec(attributes.get("ambient-light"));
		} else {
			ambientLight = new Vec(0, 0, 0);
		}
		
	}

	/**
	 * Send ray return the nearest intersection. Return null if no intersection
	 * 
	 * @param ray
	 * @return
	 */
	public Intersection findIntersection(Ray ray) {
		
		double minDistance = Double.MAX_VALUE;
		Surface minObject = null;
		Point3D minPoint = null;
		Point3D p;
		
		// Iterate through all objects in the scene
		for (Surface obj : surfaces) {
			
			// Find their intersection with the object
			if (obj instanceof Disc) {
				p = Intersection.rayDiscIntersection(ray, (Disc)obj);
			} else if (obj instanceof Sphere) {
				p = Intersection.raySphereIntersection(ray, (Sphere)obj);
			} else {
				p = Intersection.rayPolyIntersection(ray, (Poly)obj);
			}
			
			// If no intersection happened, skip to the next object
			if (p == null) {
				continue;
			}
			
			// Calculate the distance between the beginning of the ray
			// and the intersection point with the sphere
			double dist = Point3D.distance(ray.p, p);
			
			// If its closer than the current minimum, save it
			if (dist < minDistance) {
				minDistance = dist;
				minObject = obj;
				minPoint = p;
			}
			
		}
		
		// If no intersection happened, return null
		if (minObject == null) {
			return null;
		}
		
		// Else, return the intersection
		return new Intersection(minObject, minPoint);
	}

	public Vec calcColor(Ray ray, int level) {
		
		// Find the intersection of the ray with the closest object in the scene
		Intersection intersection = findIntersection(ray);
		
		// No intersection, return bgTexture or bgColor
		if (intersection == null) {
			return bgColor;	
		}
		
		// Initial color is black (0, 0, 0)
		// I = Iemission + Iambient + Idiffuse + Ispecular
		Vec color = new Vec(); 	
		
		// Add emission factor
		color.add(calcEmissionColor(intersection));
		
		// Add ambient factor
		color.add(calcAmbientColor(intersection));
		
		// Iterate over all the lights in the scene
		for (Light light : lights) {

			// Add diffuse factor
			color.add(calcDiffuseColor(intersection, light));
			
			// Add specular factor
			color.add(calcSpecularColor(intersection, light));
			
		}
		
		return color;
	}

	/**
	 * Add objects to the scene by name.
	 * Object can be of type Light or type Surface.
	 * 
	 * @param name - Object's name
	 * @param attributes - Object's attributes
	 */
	public void addObjectByName(String name, Map<String, String> attributes) {
		
		// Avoid unnecessary errors
		name = name.toLowerCase();
		
		// Is it Light or is it Surface?
		if (Light.isLight(name)) {
			
			Light light;
			
			if (name.equals("dir-light")) {
				light = new DirLight(attributes);
			} else if (name.equals("omni-light")) {
				light = new OmniLight(attributes);
			} else {
				light = new SpotLight(attributes);
			}
			
			lights.add(light);
			
		} else {
			
			Surface surface;
			
			if (name.equals("sphere")) {
				surface = new Sphere(attributes);
			} else if (name.equals("disc")) {
				surface = new Disc(attributes);
			} else {
				surface = new Poly(attributes);
			}
			
			surfaces.add(surface);
			
		}

	}

	/**
	 * Initialize the camera
	 * @param attributes - user attributes for Camera
	 */
	public void setCameraAttributes(Map<String, String> attributes) {
		camera.init(attributes);
	}
	
	private Vec calcEmissionColor(Intersection intersection) {
		return intersection.object.getEmissionCoefficient();
	}
	
	private Vec calcAmbientColor(Intersection intersection) {
		return Vec.scale(intersection.object.getAmbientCoefficient(), ambientLight);
	}
	
	private Vec calcDiffuseColor(Intersection intersection, Light light) {
		
		Surface object = intersection.object;
		Point3D point  = intersection.point;
		
		// Find the normal at the intersection point
		Vec N = object.getNormalAtPoint(point);
		
		// Find the vector between the intersection point
		// and the light source, and IL at that point
		Vec L = null;
		Vec IL = null;
		if (light instanceof DirLight) {
			DirLight dLight = (DirLight)light; 
			L = dLight.getDirection();
			L.negate();
			IL = dLight.getIntensityAtPoint(point);
		} else if (light instanceof OmniLight) {
			OmniLight oLight = (OmniLight)light;
			L = Point3D.vectorBetweenTwoPoints(point, oLight.getPosition());
			IL = oLight.getIntensityAtPoint(point);
		} else {
			SpotLight sLight = (SpotLight)light;
			L = Point3D.vectorBetweenTwoPoints(point, sLight.getPosition());
			IL = sLight.getIntensityAtPoint(point);
		}
		L.normalize();
		
		// Calculate the dot product between them
		double dotProduct = Vec.dotProd(N, L);
		
		// Get the surface's diffuse coefficient
		Vec KD = object.getDiffuseCoefficient();
		
		// Calculate ID
		Vec ID = Vec.scale(KD, Vec.scale(dotProduct, IL));
		
		return ID;
		
	}
	
	private Vec calcSpecularColor(Intersection intersection, Light light) {
		
		Surface object = intersection.object;
		Point3D point  = intersection.point;
		
		// Find the normal at the intersection point
		Vec N = object.getNormalAtPoint(point);
		
		// Find the vector between the intersection point
		// and the light source, and IL at that point
		Vec L = null;
		Vec IL = null;
		if (light instanceof DirLight) {
			DirLight dLight = (DirLight)light; 
			L = dLight.getDirection();
			L.negate();
			IL = dLight.getIntensityAtPoint(point);
		} else if (light instanceof OmniLight) {
			OmniLight oLight = (OmniLight)light;
			L = Point3D.vectorBetweenTwoPoints(point, oLight.getPosition());
			IL = oLight.getIntensityAtPoint(point);
		} else {
			SpotLight sLight = (SpotLight)light;
			L = Point3D.vectorBetweenTwoPoints(point, sLight.getPosition());
			IL = sLight.getIntensityAtPoint(point);
		}
		L.normalize();
		
		// Reflect L in relation to N
		Vec R = L.reflect(N);
		
		// Calculate the dot product between them
		double dotProduct = Vec.dotProd(N, L);
		
		// Raise it to the power of n
		double dotProductN = Math.pow(dotProduct, object.getShininessCoefficient());
		
		// Get the surface's specular coefficient
		Vec KS = object.getSpecularCoefficient();
		
		// Calculate IS
		Vec IS = Vec.scale(KS, Vec.scale(dotProductN, IL));
		
		return IS;
		
	}
}
