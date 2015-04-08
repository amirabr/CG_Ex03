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
	private double screenHeight;
	//private double frustum;
	
	public Camera(Map<String, String> attributes) {
		
		// Initialize attributes from XML
		init(attributes);
		
		// Make sure we have an orthogonal basis
		right = Vec.crossProd(towards, up);
		if (Vec.dotProd(towards, up) != 0) {
			up = Vec.crossProd(towards, right);
		}
		
		// Make sure we have an orthonormal basis
		towards.normalize();
		up.normalize();
		right.normalize();
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
		
		// Initialize 'up' attribute
		if (!attributes.containsKey("up-direction")) {
			throw new IllegalArgumentException("Missing 'up-direction' attribute");
		}
		up = new Vec(attributes.get("up-direction"));
	
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
		
		Point3D centerPixel2D; 	// Coordinates of central pixel of view plane in the view plane (2D)
		Point3D centerPixel3D;  // Coordinates of central pixel of view plane in the scene (3D)
		Point3D desiredPixel3D; // Coordinates of desired pixel in the scene (3D)
		
		double pixelRatio; 		// The actual size of a pixel in the scene
		
		Vec goUp; 				// How much to travel up to get from centerPixel to desiredPixel
		Vec goRight; 			// How much to travel right to get from centerPixel to desiredPixel
		Vec desiredVector; 		// The vector from eye to desiredPixel
		
		pixelRatio = screenWidth / width;
		screenHeight = height * pixelRatio;
		centerPixel2D = new Point3D(Math.floor(screenWidth/2), Math.floor(screenHeight/2), 0);
		centerPixel3D = eye.addVector(Vec.scale(screenDist, towards));
		
		goUp 	= Vec.scale(y - centerPixel2D.y, Vec.scale(pixelRatio, up));
		goRight = Vec.scale(x - centerPixel2D.x, Vec.scale(pixelRatio, right));
		desiredPixel3D = centerPixel3D.addVector(goUp).addVector(goRight);
		desiredVector = Point3D.vectorBetweenTwoPoints(eye, desiredPixel3D);
		
		return new Ray(eye, desiredVector);
	}

}
