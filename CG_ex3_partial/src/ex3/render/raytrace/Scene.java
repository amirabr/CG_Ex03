package ex3.render.raytrace;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

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
	private String bgTexturePath; 		// Background image of the scene
	private int maxRecLvl; 				// Max number of recursive rays when calculating reflections
	private Vec ambientLight; 			// Ambient light of the scene
	
	private File scenePath; 			// Path to scene files
	private int canvasWidth; 			// Canvas width (used for texture calculations)
	private int canvasHeight; 			// Canvas height (used for texture calculations)
	private double wRatio; 				// Canvas-texture width ratio
	private double hRatio; 				// Canvas-texture height ratio
	private BufferedImage bgTexture; 	// Actual background texture image
	
	protected List<Surface> surfaces; 	// All of the surfaces in the scene
	protected List<Light> lights; 	 	// All of the lights in the scene
	protected Camera camera; 			// The camera of the scene

	/**
	 * Constructor.
	 * 
	 * @param canvasWidth - the canvas width
	 * @param canvasHeight - the canvas height
	 * @param path - the scene path
	 */
	public Scene(int canvasWidth, int canvasHeight, File path) {

		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		this.scenePath = path;
		
		surfaces = new LinkedList<Surface>(); 	// No surfaces
		lights = new LinkedList<Light>(); 		// No lights
		camera = new Camera(); 					// Empty camera
		
	}

	/**
	 * Initialize attributes from XML.
	 * 
	 * @param attributes - user attributes for Scene
	 */
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
			bgTexturePath = attributes.get("background-tex");
		} else {
			bgTexturePath = null;
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
		
		// Initialize the background texture
		initBgTexture();
		
	}
	
	/**
	 * Initialize the background texture.
	 * Read image to memory and calculate ratios.
	 */
	private void initBgTexture() {
		
		if (bgTexturePath != null) {    
			try {
				bgTexture = ImageIO.read(new File(scenePath.getParent() + File.separator + bgTexturePath));
				wRatio = 1.0 * bgTexture.getWidth() / canvasWidth;
				hRatio = 1.0 * bgTexture.getHeight() / canvasHeight;
			} catch (IOException e1) {
				System.out.println("Error: Could not read texture file.");
			}
		}
		
	}

	/**
	 * Shoot the ray into the scene, see if it hits anything.
	 * If it hits multiple objects, return the closest one.
	 * If it hits nothing, return null.
	 * 
	 * @param ray - the ray
	 * @return intersecting point and object
	 */
	public Intersection findIntersection(Ray ray) {
		
		double minDistance = Double.POSITIVE_INFINITY;
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
			if ((dist < minDistance) && (dist > Intersection.TOLERANCE)) {
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
		return new Intersection(minObject, minPoint, minDistance);
		
	}

	/**
	 * Calculate the color where the ray points at.
	 * If it hits an object, calculate the color there.
	 * If it hits nothing, calculate the background color/texture.
	 *  
	 * @param ray - the ray
	 * @param level - current recursion level
	 * @param x - the x coordinate of the currently drawn pixel
	 * @param y - the y coordinate of the currently drawn pixel
	 * @return the color at that point
	 */
	public Vec calcColor(Ray ray, int level, int x, int y) {
		
		// Recursion stopping condition
		if (level == maxRecLvl) {
			return new Vec(0, 0, 0);
		}
		
		// Find the intersection of the ray with the closest object in the scene
		Intersection intersection = findIntersection(ray);
		
		// No intersection, return bgColor or bgTexture
		if (intersection == null) {
			
			if (bgTexture == null) {
				
				// If no background texture is defined, return the background color
				return bgColor;
				
			} else {
				
				// If background texture is defined, return the proper pixel from it
				int rgb = bgTexture.getRGB((int)(x * wRatio), (int)(y * hRatio));
				int red =   (rgb >> 16) & 0xFF;
				int green = (rgb >>  8) & 0xFF;
				int blue =  (rgb      ) & 0xFF;
				return new Vec(red/255.0, green/255.0, blue/255.0);
				
			}
			
		}
		
		// Initial color is black (0, 0, 0)
		// I = Iemission + Iambient + Idiffuse + Ispecular + Ireflective
		Vec color = new Vec(); 	
		
		// Add emission factor
		color.add(calcEmissionColor(intersection));
		
		// Add ambient factor
		color.add(calcAmbientColor(intersection));
		
		// Iterate over all the lights in the scene
		for (Light light : lights) {

			// Check shadow
			boolean occluded = false;
			if (!(light instanceof DirLight)) { 	// Directional light doesn't cast a shadow
				Vec fromIntersectionToLightSource = light.vectorToMe(intersection.point);
				Ray shadowRay = new Ray(intersection.point, fromIntersectionToLightSource);
				Intersection lightIntersection = findIntersection(shadowRay);
				if (lightIntersection != null) {
					double distanceToLightSource = light.distanceToMe(intersection.point);
					double distanceToObject = lightIntersection.distance;
					if (distanceToObject > Intersection.TOLERANCE && distanceToLightSource > distanceToObject + Intersection.TOLERANCE) {
						occluded = true;
					}
				}
			}
			
			// If point is not shaded
			if (!occluded) {
			
				// Add diffuse factor
				color.add(calcDiffuseColor(intersection, light));
				
				// Add specular factor
				color.add(calcSpecularColor(intersection, light, ray));
				
			}
			
		}

		// Add reflective factor
		Vec normal = intersection.object.getNormalAtPoint(intersection.point);
		Ray reflectionRay = new Ray(intersection.point, ray.v.reflect(normal));
		double KS = intersection.object.getReflectanceCoefficient();
		Vec reflectionColor = calcColor(reflectionRay, level+1, x, y);
		color.add(Vec.scale(KS, reflectionColor));
		
		// Make sure we don't overflow on color
		if (color.x > 1) {
			color.x = 1;
		}
		if (color.y > 1) {
			color.y = 1;
		}
		if (color.z > 1) {
			color.z = 1;
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
	 * Initialize the camera.
	 * 
	 * @param attributes - user attributes for Camera
	 */
	public void setCameraAttributes(Map<String, String> attributes) {
		camera.init(attributes);
	}
	
	/**
	 * Calculate the amount of emission color at the intersection point.
	 * 
	 * @param intersection point
	 * @return emission factor
	 */
	private Vec calcEmissionColor(Intersection intersection) {
		return intersection.object.getEmissionCoefficient();
	}
	
	/**
	 * Calculate the amount of ambient color at the intersection point.
	 * 
	 * @param intersection point
	 * @return ambient factor
	 */
	private Vec calcAmbientColor(Intersection intersection) {
		return Vec.scale(intersection.object.getAmbientCoefficient(), ambientLight);
	}
	
	/**
	 * Calculate the amount of diffuse color at the intersection point,
	 * by the specified light source.
	 * 
	 * @param intersection point
	 * @return diffuse factor
	 */
	private Vec calcDiffuseColor(Intersection intersection, Light light) {
		
		Surface object = intersection.object;
		Point3D point  = intersection.point;
		
		// Find the normal at the intersection point
		Vec N = object.getNormalAtPoint(point);
		
		// Find the vector between the intersection point
		// and the light source, and normalize
		Vec L = light.vectorToMe(point);
		L.normalize();
		
		// Find IL at that point
		Vec IL = light.getIntensityAtPoint(point);
		
		// Calculate the dot product between them
		// Note: cosine is negative if angle>90, hence the max()
		double dotProduct = Math.max(0, Vec.dotProd(N, L));
		
		// Get the surface's diffuse coefficient
		Vec KD = object.getDiffuseCoefficient();
		
		// Calculate ID
		Vec ID = Vec.scale(KD, Vec.scale(dotProduct, IL));
		
		return ID;
		
	}
	
	/**
	 * Calculate the amount of specular color at the intersection point,
	 * by the specified light source.
	 * 
	 * @param intersection point
	 * @return specular factor
	 */
	private Vec calcSpecularColor(Intersection intersection, Light light, Ray ray) {
		
		Surface object = intersection.object;
		Point3D point  = intersection.point;
		
		// Find the normal at the intersection point
		Vec N = object.getNormalAtPoint(point);

		// Find the vector between the intersection point
		// and the light source, and normalize
		Vec L = light.vectorToMe(point);
		L.normalize();
		
		// Find IL at that point
		Vec IL = light.getIntensityAtPoint(point);
		
		// Reflect L in relation to N, and normalize
		Vec R = L.reflect(N);
		R.normalize();
		
		// Find the vector from the starting point to the intersection point, and normalize
		Vec V = ray.v; 
		
		// Calculate the dot product between them
		// Note: cosine is negative if angle>90, hence the max()
		double dotProduct = Math.max(0, Vec.dotProd(V, R));
		
		// Raise it to the power of n (shininess)
		double dotProductN = Math.pow(dotProduct, object.getShininessCoefficient());
		
		// Get the surface's specular coefficient
		Vec KS = object.getSpecularCoefficient();
		
		// Calculate IS
		Vec IS = Vec.scale(KS, Vec.scale(dotProductN, IL));
		
		return IS;
		
	}
	
	public Ray castRay(double x, double y, double width, double height) {
		return camera.constructRayThroughPixel(x, y, width, height);
	}
	
}
