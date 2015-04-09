package shapes;

import math.Point3D;
import math.Ray;
import math.Vec;

public class Intersections {
	
	public static Point3D raySphereIntersection(Ray ray, Sphere sphere) {
		
		Vec fromOtoP0 = Point3D.vectorBetweenTwoPoints(sphere.getCenter(), ray.p);
		double a = 1;
	    double b = 2 * Vec.dotProd(ray.v, fromOtoP0); 
	    double c = fromOtoP0.lengthSquared() - sphere.getRadius();
	    double discriminant = (b * b - 4 * a * c);
	    double d = Math.sqrt(discriminant);
	    
	    // No solution exists
	    if (discriminant < 0.0) {
	        return null;
	     }
		
	    // a solution exists, find it
	    double t1 = +(-b + d) / (2.0 * a);
        double t2 = +(-b - d) / (2.0 * a);
        
        // If t < 0 the intersection is behind me, ignore it
        if (t1 < 0 && t2 < 0) {
        	return null;
        }
        
        // Find the closest t of the two
        double minT = Math.min(t1, t2);
        
        // Return the point of intersection
        return Point3D.addVectorToPoint(ray.p, Vec.scale(minT, ray.v));
        
	}
	
	public static Point3D rayDiscIntersection(Ray ray, Disc disc) {
		return null;
	}
	
	public static Point3D rayPolyIntersection(Ray ray, Poly poly) {
		return null;
	}

}
