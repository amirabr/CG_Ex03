package math;

/**
 * Represents a color, using RGB coordinates.
 * Each color channel is a value between 0 and 1.
 * To get the real value, multiply by 255.
 *
 */
public class RGB extends Tuple {
	
	/**
	 * Default constructor, initializes RGB to (0, 0, 0).
	 */
	public RGB() {
		super();
	}
	
	/**
	 * Constructor, initializes RGB to given color.
	 * @param red - red amount
	 * @param green - green amount
	 * @param blue - blue amount
	 */
	public RGB(double red, double green, double blue) {
		super(red, green, blue);
	}
	
	/**
	 * Constructor, initializes RGB to given color, in string representation.
	 * @param rgb - string representation of color
	 */
	public RGB(String rgb) {
		super(rgb);
	}
	
	/**
	 * Copy constructor, initializes RGB to given color, from another RGB.
	 * @param rgb - other RGB
	 */
	public RGB(RGB rgb) {
		super(rgb);
	}
	
	/**
	 * X coordinate is RED 
	 * @return this.x
	 */
	public double red() {
		return x;
	}
	
	/**
	 * Y coordinate is GREEN 
	 * @return this.y
	 */
	public double green() {
		return y;
	}
	
	/**
	 * Z coordinate is BLUE 
	 * @return this.z
	 */
	public double blue() {
		return z;
	}
	
	/**
	 * Get the real red amount. 
	 * @return x * 255
	 */
	public double realRed() {
		return red() * 255;
	}
	
	/**
	 * Get the real green amount. 
	 * @return y * 255
	 */
	public double realGreen() {
		return green() * 255;
	}
	
	/**
	 * Get the real blue amount. 
	 * @return z * 255
	 */
	public double realBlue() {
		return blue() * 255;
	}
	
	public void add(RGB color) {
		this.x += color.x;
		this.y += color.y;
		this.z += color.z;
	}
	
	/**
	 * Scale the RGB vector by a scalar s.
	 * @param s - the scalar
	 * @param rgb - the RGB vector
	 * @return the scaled RGB vector
	 */
	public static RGB scale(double s, RGB rgb) {
		return new RGB(rgb.red() * s, rgb.green() * s, rgb.blue() * s);
	}

}
