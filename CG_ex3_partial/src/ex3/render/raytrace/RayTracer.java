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
	private File filePath;
	
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
		
		scene = new Scene();
		scene.init(sceneDesc.getSceneAttributes());
		
		for (Element e : sceneDesc.getObjects()) {
			scene.addObjectByName(e.getName(), e.getAttributes());
		}
		
		scene.setCameraAttributes(sceneDesc.getCameraAttributes());
		
		this.canvasWidth = width;
		this.canvasHeight = height;
		this.filePath = path;
		
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
		
		for (int i=0; i<canvasWidth; i++) {
			
			if (i==480/2 && line==360/2) {
			//if (i==480/2+25 && line==360/2+50) {
				System.out.println("stop here");
				//canvas.setRGB(i, line, new Color(255, 0, 0).getRGB());
				//continue;
			}
			
			Ray ray = scene.castRay(i, line, canvasWidth, canvasHeight);
			Vec color = scene.calcColor(ray, 0);
			try {
			Color realColor = new Color((int)(color.x*255), (int)(color.y*255), (int)(color.z*255));
			canvas.setRGB(i, line, realColor.getRGB());
			} catch (IllegalArgumentException ex) {
				System.out.println("color overflow");
			}
			
		}
		
	}

}
