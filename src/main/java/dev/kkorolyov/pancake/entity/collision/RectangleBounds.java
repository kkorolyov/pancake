package dev.kkorolyov.pancake.entity.collision;

/**
 * Bounds defined by a rectangular area.
 */
public class RectangleBounds implements Bounds {
	private final Vector origin, direction;
	
	/**
	 * Constructs a new rectangle from origin coordinates, width, and height.
	 * @param x origin point x-coordinate
	 * @param y origin point y-coordinate
	 * @param width rectangle width
	 * @param height rectangle height
	 */
	public RectangleBounds(float x, float y, float width, float height) {
		this(new Vector(x, y), width, height);
	}
	/**
	 * Constructs a new rectangle from an origin point, width, and height.
	 * @param origin origin point of rectangle
	 * @param width rectangle width
	 * @param height rectangle height
	 */
	public RectangleBounds(Vector origin, float width, float height) {
		this(origin, new Vector(origin.getX() + width, origin.getY() + height));
	}
	/**
	 * Constructs a new rectangle from the area between 2 corner points.
	 * @param origin origin point of rectangle
	 * @param vector point of corner diagonally opposite {@code origin}
	 */
	public RectangleBounds(Vector origin, Vector vector) {
		this.origin = origin;
		this.direction = vector;
	}
	
	/**
	 * Translates this rectangle by specified values.
	 * @param dx x-coordinate change
	 * @param dy y-coordinate change
	 * @return this rectangle after translation
	 */
	public RectangleBounds translate(float dx, float dy) {
		origin.translate(dx, dy);
		direction.translate(dx, dy);
		
		return this;
	}
	
	@Override
	public boolean intersects(Bounds other) {
		if (other instanceof RectangleBounds)
			return intersects((RectangleBounds) other);
		else
			return false;
	}
	private boolean intersects(RectangleBounds other) {
		return intersects(origin.getX(), direction.getX(), other.origin.getX(), other.direction.getX())
				&& intersects(origin.getY(), direction.getY(), other.origin.getY(), other.direction.getY());
	}
	private static boolean intersects(float x1, float x2, float y1, float y2) {
		return x2 >= y1 && y2 >= x1;
	}
	
	@Override
	public Vector getOrigin() {
		return origin;
	}
	
	/** @return origin x-coordinate */
	public float getX() {
		return origin.getX();
	}
	/** @return origin y-coordinate */
	public float getY() {
		return origin.getY();
	}
	
	/** @return rectangle width */
	public float getWidth() {
		return direction.getX() - origin.getX();
	}
	/** @return rectangle height */
	public float getHeight() {
		return direction.getY() - origin.getY();
	}
}
