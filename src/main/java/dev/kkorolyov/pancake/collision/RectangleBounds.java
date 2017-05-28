package dev.kkorolyov.pancake.collision;

import dev.kkorolyov.pancake.component.Bounds;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Bounds defined by a rectangular area.
 */
public class RectangleBounds implements Bounds {
	private final Vector origin, size;
	
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
		this.origin = origin;
		size = new Vector(width, height);
	}
	
	/**
	 * Translates this rectangle by specified values.
	 * @param dx x-coordinate change
	 * @param dy y-coordinate change
	 */
	public void translate(float dx, float dy) {
		origin.translate(dx, dy);
	}
	
	@Override
	public boolean intersects(Bounds other) {
		if (other instanceof RectangleBounds)
			return intersects((RectangleBounds) other);
		else
			return false;
	}
	private boolean intersects(RectangleBounds other) {
		return intersects(getX(), getX() + getWidth(), other.getX(), other.getX() + other.getWidth())
				&& intersects(getY(), getY() + getHeight(), other.getY(), other.getY() + other.getHeight());
	}
	private static boolean intersects(float x1, float x2, float y1, float y2) {
		return x2 >= y1 && y2 >= x1;
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
		return size.getX();
	}
	/** @return rectangle height */
	public float getHeight() {
		return size.getY();
	}
	
	@Override
	public String toString() {
		return "origin=" + origin + " size=" + size;
	}
}
