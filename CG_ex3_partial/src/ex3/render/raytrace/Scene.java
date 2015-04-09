package ex3.render.raytrace;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import shapes.Disc;
import shapes.Poly;
import shapes.Sphere;
import shapes.Surface;
import lights.DirLight;
import lights.Light;
import lights.OmniLight;
import lights.SpotLight;
import math.Point3D;
import math.RGB;
import math.Ray;
import math.Vec;

/**
 * A Scene class containing all the scene objects including camera, lights and surfaces.
 * 
 */
public class Scene implements IInitable {

	private RGB bgColor; 				// Background color of the scene
	private String bgTexture; 			// Background image of the scene
	private int maxRecLvl; 				// Max number of recursive rays when calculating reflections
	private RGB ambientLight; 			// Ambient light of the scene
	
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
			bgColor = new RGB(attributes.get("background-col"));
		} else {
			bgColor = new RGB(0, 0, 0);
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
			ambientLight = new RGB(attributes.get("ambient-light"));
		} else {
			ambientLight = new RGB(0, 0, 0);
		}
		
	}

	/**
	 * Send ray return the nearest intersection. Return null if no intersection
	 * 
	 * @param ray
	 * @return
	 */
	public void findIntersection(Ray ray) {
		//TODO find ray intersection with scene, change the output type, add whatever you need
	}

	public Vec calcColor(Ray ray, int level) {
		//TODO implement ray tracing recursion here, add whatever you need
		return null;
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
}
