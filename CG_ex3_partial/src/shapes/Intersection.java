package shapes;

import math.Point3D;
import math.Ray;
import math.Vec;

/**
 * An Intersection class to represent an intersection between rays and objects.
 *
 */
public class Intersection {
	
	public static final double TOLERANCE = 0.0001; 	// Tolerance level for intersections
	
	public Surface object; 							// The object I intersected with
	public Point3D point; 							// The point of intersection
	public double distance; 						// The distance from the origin point
													// to the intersection point
	
	/**
	 * Constructor.
	 */
	public Intersection() {
		
	}
	
	/**
	 * Constructor.
	 * 
	 * @param object
	 * @param point
	 * @param distance
	 */
	public Intersection(Surface object, Point3D point, double distance) {
		this.object = object;
		this.point = point;
		this.distance = distance;
	}
	
	/**
	 * Ray-sphere intersection algorithm.
	 * Returns the point of intersection if the ray intersects with the sphere, null otherwise.
	 * 
	 * @param ray - the ray
	 * @param sphere - the sphere
	 * @return the intersection point if exists, null otherwise
	 */
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
        
        // If both t's are behind me, the intersection is behind me, ignore it
        if (t1 <= 0 && t2 <= 0) {
        	return null;
        }
        
        // If t1 is in front of me, and t2 is behind me, take t1
        if (t1 > 0 && t2 <= 0) {
        	return Point3D.addVectorToPoint(ray.p, Vec.scale(t1, ray.v));
        }
        
        // If t2 is in front of me, and t1 is behind me, take t2
        if (t2 > 0 && t1 <= 0) {
        	return Point3D.addVectorToPoint(ray.p, Vec.scale(t2, ray.v));
        }
        
        // If both t's are in front of me, the intersection is in front of me, take the closer one
        return Point3D.addVectorToPoint(ray.p, Vec.scale(Math.min(t1, t2), ray.v));
        
	}
	
	/**
	 * Ray-disc intersection algorithm.
	 * Returns the point of intersection if the ray intersects with the disc, null otherwise.
	 * 
	 * @param ray - the ray
	 * @param disc - the disc
	 * @return the intersection point if exists, null otherwise
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
	
	/**
	 * Ray-poly intersection algorithm.
	 * Returns the point of intersection if the ray intersects with the poly, null otherwise.
	 * 
	 * @param ray - the ray
	 * @param poly - the poly
	 * @return the intersection point if exists, null otherwise
	 */
	public static Point3D rayPolyIntersection(Ray ray, Poly poly) {
		
		// Check if the ray intersects with the disc's plane
		Point3D intersection = raySurfaceIntersection(ray, poly.getNormalAtPoint(null), poly.getPoint(0));
		
		// If it doesn't hit the plane, it can't hit the poly
		if (intersection == null) {
			return null;
		}
		
		// Calculate the vector from start of ray to the intersection point
	    Vec fromP0toP = Point3D.vectorBetweenTwoPoints(ray.p, intersection);
		
		// Now we need to make sure the intersection happened inside the poly.
		// We connect the beginning of the ray with all the poly's points, thus
		// creating a sort of pyramid with many sides. We calculate the normal
		// of each side and check that its dot product with the ray is greater 
		// than or equal to zero. If so, then the ray hits the inside of the poly.
		for (int i=0; i<poly.getSize(); i++) {
			
			Vec v1 = Point3D.vectorBetweenTwoPoints(ray.p, poly.getPoint(i));
			Vec v2 = Point3D.vectorBetweenTwoPoints(ray.p, poly.getPoint((i+1)%poly.getSize()));
			Vec sideNormal = Vec.crossProd(v2, v1);
			if (Vec.dotProd(fromP0toP, sideNormal) < 0) {
				return null;
			}
			
		}
		
		// All the conditions held, the intersection point is correct!
		return intersection;
		
	}
	
	/**
	 * Ray-surface intersection algorithm.
	 * Returns the point of intersection if the ray intersects with the surface, null otherwise.
	 * 
	 * @param ray - the ray
	 * @param surfaceNormal - a normal to the surface
	 * @param pointOnSurface - a point of the surface
	 * @return the intersection point if exists, null otherwise
	 */
	private static Point3D raySurfaceIntersection(Ray ray, Vec surfaceNormal, Point3D pointOnSurface) {
		
		// First, check if the surface is facing the ray
		double rayDotNormal = Vec.dotProd(ray.v, surfaceNormal);
		if (rayDotNormal >= 0) {
			
			// The surface is facing away from the ray.
			// We hit the back-face, ignore it
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
