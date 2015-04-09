package ex3.render.raytrace;

import shapes.Disc;
import shapes.Sphere;
import shapes.Poly;
import shapes.Surface;

public class TestMain {

	public static void main(String[] args) {
		
		Surface s = new Sphere();
		if (s instanceof Disc) {
			System.out.println("yes");
		} else {
			System.out.println("no");
		}

	}

}
