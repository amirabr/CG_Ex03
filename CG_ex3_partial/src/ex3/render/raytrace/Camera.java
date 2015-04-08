package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;


/**
 * Represents the scene's camera.
 * 
 */
public class Camera implements IInitable{

	private Point3D eye;
	
	private Vec towards;
	private Vec up;
	private Vec right;
	
	private double screenDist;
	private double screenWidth;
	//private double frustum;
	
	public Camera(Map<String, String> attributes) {
		init(attributes);
	}
	
	public void init(Map<String, String> attributes) throws IllegalArgumentException {
		
		// Initialize 'eye' attribute
		if (!attributes.containsKey("eye")) {
			throw new IllegalArgumentException("Missing 'eye' attribute");
		}
		eye = new Point3D(attributes.get("eye"));
		
		// Initialize 'towards' attribute
		if (!attributes.containsKey("direction") && !attributes.containsKey("look-at")) {
			throw new IllegalArgumentException("Missing 'direction' or 'look-at' attribute");
		}
		if (attributes.containsKey("direction")) {
			towards = new Vec(attributes.get("direction"));
		} else {
			towards = eye.vectorToAnotherPoint(new Point3D(attributes.get("look-at")));
		}
		
		// Initialize 'up' and 'right' attribute
		// Make sure we have an orthonormal basis
		if (!attributes.containsKey("up-direction")) {
			throw new IllegalArgumentException("Missing 'up-direction' attribute");
		}
		Vec tmpUp = new Vec(attributes.get("up-direction"));
		right = Vec.crossProd(towards, tmpUp);
		if (Vec.dotProd(towards, tmpUp) == 0) {
			up = tmpUp;
		} else {
			up = Vec.crossProd(towards, right);
		}
	
		// Initialize 'screen-dist' attribute
		if (!attributes.containsKey("screen-dist")) {
			throw new IllegalArgumentException("Missing 'screen-dist' attribute");
		}
		screenDist = Double.parseDouble(attributes.get("screen-dist"));
		
		// Initialize 'screen-dist' attribute
		// Default is 2.0
		if (!attributes.containsKey("screen-width")) {
			screenWidth = 2.0;
		} else {
			screenWidth = Double.parseDouble(attributes.get("screen-width"));
		}
		
	}
	
	/**
	 * Transforms image xy coordinates to view pane xyz coordinates. Returns the
	 * ray that goes through it.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Ray constructRayThroughPixel(double x, double y, double height, double width) {		
		
		Point3D centerPixel2D; 	// Coordinate of central pixel of view plane in the view plane (2D)
		Point3D centerPixel3D;  // Coordinate of central pixel of view plane in the scene (3D)
		double pixelRatio;
		
		centerPixel3D = eye.addVector(towards);
		
		return null;
	}

	
	
	
	
	
	
	
}
