package lights;

import math.Point3D;
import math.RGB;
import ex3.render.raytrace.IInitable;

public abstract class Light implements IInitable {

	protected RGB color;
	
	public abstract RGB getIntensityAtPoint(Point3D p);
	
}
