package shapes;

import math.Point3D;
import math.Ray;
import math.Vec;

public class Intersection {
	
	public static final double TOLERANCE = 1.0E-4D;
	
	public Surface object;
	public Point3D point;
	public double distance;
	
	public Intersection() {
		
	}
	
	public Intersection(Surface object, Point3D point, double distance) {
		this.object = object;
		this.point = point;
		this.distance = distance;
	}
	
	public static Point3D raySphereIntersection(Ray ray, Sphere sphere, boolean showInside) {
		
		Vec fromOtoP0 = Point3D.vectorBetweenTwoPoints(sphere.getCenter(), ray.p);
		double a = 1;
	    double b = 2 * Vec.dotProd(ray.v, fromOtoP0);
	    double c = fromOtoP0.lengthSquared() - Math.pow(sphere.getRadius(), 2);
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
        
        // Here t1 < 0 and t2 > 0
        // This means i'm inside the sphere, with t1 behind me and t2 in front of me,
        // so i'm facing the inside of the sphere
        if (t1 < 0) {
        	
        	// If I want to see the inside, return t2
        	if (showInside) {
        		return Point3D.addVectorToPoint(ray.p, Vec.scale(t2, ray.v));
        	}
        	
        	// If not, ignore it
        	return null;
        	
        }
        
        // Here t1 > 0 and t2 < 0
        // This means i'm inside the sphere again, this time pointing the other way
        // Same deal as before
        if (t2 < 0) {
        	
        	// If I want to see the inside, return t1
        	if (showInside) {
        		return Point3D.addVectorToPoint(ray.p, Vec.scale(t1, ray.v));
        	}
        	
        	// If not, ignore it
        	return null;
        	
        }
        
        // Here t1 > 0 and t2 > 0
        // This means i'm outside the sphere, facing it, with 2 intersecting points
        // If I want to see the inside, return the further t (the bigger one)
        // If I want to see the outside, return the closer t (the smaller one)
        if (showInside) {
        	return Point3D.addVectorToPoint(ray.p, Vec.scale(Math.max(t1, t2), ray.v));
        } else {
        	return Point3D.addVectorToPoint(ray.p, Vec.scale(Math.min(t1, t2), ray.v));
        }
        
	}
	
	/*
	public static Point3D raySphereIntersection(Ray ray, Sphere sphere) {
		
		Vec fromOtoP0 = Point3D.vectorBetweenTwoPoints(sphere.getCenter(), ray.p);
		double a = 1;
	    double b = 2 * Vec.dotProd(ray.v, fromOtoP0);
	    double c = fromOtoP0.lengthSquared() - Math.pow(sphere.getRadius(), 2);
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
	*/
	
	public static Point3D rayDiscIntersection(Ray ray, Disc disc) {
		
		// Check if the ray intersects with the disc's plane
		Point3D intersection = raySurfaceIntersection(ray, disc.getNormalAtPoint(null), disc.getCenter());
		
		// If it doesn't hit the plane, it can't hit the disc
		if (intersection == null) {
			return null;
		}
		
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
		
		// Check if the ray intersects with the disc's plane
		Point3D intersection = raySurfaceIntersection(ray, poly.getNormalAtPoint(null), poly.getPoint(0));
		
		// If it doesn't hit the plane, it can't hit the poly
		if (intersection == null) {
			return null;
		}
		
		// Now we need to make sure the intersection happened inside the poly.
		// We connect the beginning of the ray with all the poly's points, thus
		// creating a sort of pyramid with many sides. We calculate the normal
		// of each side and check that its dot product with the ray is greater 
		// than or equal to zero. If so, then the ray hits the inside of the poly.
		for (int i=0; i<poly.getSize(); i++) {
			
			Vec v1 = Point3D.vectorBetweenTwoPoints(ray.p, poly.getPoint(i));
			Vec v2 = Point3D.vectorBetweenTwoPoints(ray.p, poly.getPoint((i+1)%poly.getSize()));
			Vec sideNormal = Vec.crossProd(v2, v1);
			sideNormal.normalize();
			if (Vec.dotProd(ray.v, sideNormal) < 0) {
				return null;
			}
			
		}
		
		// All the conditions held, the intersection point is correct!
		return intersection;
	}
	
	private static Point3D raySurfaceIntersection(Ray ray, Vec surfaceNormal, Point3D pointOnSurface) {
		
		// First, check if the surface is facing the ray
		if (Vec.dotProd(ray.v, surfaceNormal) >= 0) {
			
			// The surface is facing away from the ray.
			// We hit the back-face, ignore it
			return null;
			
		}
		
		double rayDotNormal = Vec.dotProd(ray.v, surfaceNormal);
		
		// If the ray's vector and surface normal are orthogonal,
		// then the ray and the surface are parallel.
		if (rayDotNormal == 0) {
		
			// We can't see the surface, ignore it
			return null;
			
		}
		
		// If we got here, then the ray and the surface are not parallel.
		// Find the intersection of the ray with the surface plane
		Vec fromRaytoSurface = Point3D.vectorBetweenTwoPoints(ray.p, pointOnSurface);
		double fromRaytoSurfaceDotSurfaceNormal = Vec.dotProd(fromRaytoSurface, surfaceNormal);
		double d = fromRaytoSurfaceDotSurfaceNormal / rayDotNormal;
		return Point3D.addVectorToPoint(ray.p, Vec.scale(d, ray.v));
		
	}

}
