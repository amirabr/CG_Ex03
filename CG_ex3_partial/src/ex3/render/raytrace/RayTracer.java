package ex3.render.raytrace;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import math.Ray;
import math.Vec;
import ex3.parser.Element;
import ex3.parser.SceneDescriptor;
import ex3.render.IRenderer;

public class RayTracer implements IRenderer {

	private Scene scene;
	private int canvasWidth;
	private int canvasHeight;
	
	/**
	 * Constructor.
	 */
	public RayTracer() {
		
	}
	
	/**
	 * Inits the renderer with scene description and sets the target canvas to
	 * size (width X height). After init renderLine may be called
	 * 
	 * @param sceneDesc
	 *            Description data structure of the scene
	 * @param width
	 *            Width of the canvas
	 * @param height
	 *            Height of the canvas
	 * @param path
	 *            File path to the location of the scene. Should be used as a
	 *            basis to load external resources (e.g. background image)
	 */
	@Override
	public void init(SceneDescriptor sceneDesc, int width, int height, File path) {
		
		// Initialize the scene
		scene = new Scene(width, height, path);
		scene.init(sceneDesc.getSceneAttributes());
		
		// Add all the objects to the scene
		for (Element e : sceneDesc.getObjects()) {
			scene.addObjectByName(e.getName(), e.getAttributes());
		}
		
		// Set the camera
		scene.setCameraAttributes(sceneDesc.getCameraAttributes());
		
		// Get the canvas height and width
		this.canvasWidth = width;
		this.canvasHeight = height;
		
	}

	/**
	 * Renders the given line to the given canvas. Canvas is of the exact size
	 * given to init. This method must be called only after init.
	 * 
	 * @param canvas
	 *            BufferedImage containing the partial image
	 * @param line
	 *            The line of the image that should be rendered.
	 */
	@Override
	public void renderLine(BufferedImage canvas, int line) {
		
		// Iterate over all pixels in the line
		for (int i=0; i<canvasWidth; i++) {

			Vec color;
			if (scene.superSampling() == -1) {
				
				// Super sampling is off, shoot just one ray through the center of the pixel
				Ray ray = scene.castRay(i, line, canvasWidth, canvasHeight);
				color = scene.calcColor(ray, 0, i, line);
				
			} else {
				
				// Super sampling is on, shoot superSampling^2 rays through each pixel
				
				// Start from black
				color = new Vec();
				
				// Shoot superSampling^2 rays
				for (int j=0; j<scene.superSampling(); j++) {
					for (int k=0; k<scene.superSampling(); k++) {
						
						// Calculate coordinates inside the sub-pixel
						int ssx = i + (j / scene.superSampling());
						int ssy = line + (k / scene.superSampling());
						
						// Shoot the ray and calculate the color at that point
						Ray ray = scene.castRay(ssx, ssy, canvasWidth, canvasHeight);
						Vec ssColor = scene.calcColor(ray, 0, i, line);
						
						// Sum up the color
						color.add(ssColor);
					}
				}
				
				// Average out the colors of all the sub-pixels
				double weakning = 1 / Math.pow(scene.superSampling(), 2);
				color.scale(weakning);
				
			}
			
			// Calculate the actual color [0, 1] --> [0, 255]
			Color realColor = new Color((int)(color.x*255), (int)(color.y*255), (int)(color.z*255));
			
			// Paint the pixel
			canvas.setRGB(i, line, realColor.getRGB());	
	
		}
		
	}

}
