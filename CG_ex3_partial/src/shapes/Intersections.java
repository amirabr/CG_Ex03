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
		
		Vec discNormal = disc.getNormalAtPoint(null);
		double rayDotDiscNormal = Vec.dotProd(ray.v, discNormal);
		
		Vec fromRaytoDisc = Point3D.vectorBetweenTwoPoints(ray.p, disc.getCenter());
		double fromRaytoDiscDotDiscNormal = Vec.dotProd(fromRaytoDisc, discNormal);
		
		// First, check if the disc is facing the ray
		if (Vec.dotProd(ray.v, discNormal) >= 0) {
			
			// The disc is facing away from the ray.
			// We hit the back-face, ignore it
			return null;
			
		}
		
		// If the ray's vector and disc's normal are orthogonal,
		// then the ray and the disc's plane are parallel.
		if (rayDotDiscNormal == 0) {
		
			// If the vector from the beginning of the ray to the
			// center of the disc is also orthogonal to the disc's normal,
			// they are in the same plane
			if (fromRaytoDiscDotDiscNormal == 0) {
			
				// The ray and the disc are in the same plane,
				// the problem is now 2D and can be solved using
				// the ray-sphere intersection equation
				return raySphereIntersection(ray, disc);
				
			} else {
				
				// The ray and the disc are in different parallel planes,
				// there is no intersection.
				return null;
				
			}
			
		}
		
		// If we got here, then the ray and the disc are not parallel.
		// Find the intersection of the ray with the disc's plane
		double d = fromRaytoDiscDotDiscNormal / rayDotDiscNormal;
		Point3D intersection = Point3D.addVectorToPoint(ray.p, Vec.scale(d, ray.v));
		
		// Now we need to make sure the intersection happened inside the disc.
		// Calculate the distance between the intersection point and the
		// center of the disc. If it's less than or equal to the radius of the
		// disc, we're good. If not, there is no ray-disc intersection.
		double dist = Point3D.distance(intersection, disc.getCenter());
		if (dist <= disc.getRadius()) {
			return intersection;
		} else {
			return null;
		}
		
	}
	
	public static Point3D rayPolyIntersection(Ray ray, Poly poly) {
		return null;
	}

}
