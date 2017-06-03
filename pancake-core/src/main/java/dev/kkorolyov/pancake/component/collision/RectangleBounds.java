package dev.kkorolyov.pancake.component.collision;

import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Bounds defined by a rectangular area.
 */
public class RectangleBounds implements Bounds {
	private final Vector size;

	/**
	 * Constructs a new rectangle matching the dimensions of a sprite.
	 * @param sprite sprite supplying dimensions
	 */
	public RectangleBounds(Sprite sprite) {
		this((float) sprite.getImage().getWidth(), (float) sprite.getImage().getHeight());
	}
	/**
	 * Constructs a new rectangle from a width and height.
	 * @param width rectangle width
	 * @param height rectangle height
	 */
	public RectangleBounds(float width, float height) {
		size = new Vector(width, height);
	}

	@Override
	public boolean intersects(Bounds other, Vector thisOrigin, Vector otherOrigin) {
		if (other instanceof RectangleBounds) {
			RectangleBounds otherRect = (RectangleBounds) other;

			return intersects(thisOrigin.getX(), thisOrigin.getX() + getWidth(), otherOrigin.getX(), otherOrigin.getX() + otherRect.getWidth())
						 && intersects(thisOrigin.getY(), thisOrigin.getY() + getHeight(), otherOrigin.getY(), otherOrigin.getY() + otherRect.getHeight());
		} else {
			return false;
		}
	}
	private static boolean intersects(float x1, float x2, float y1, float y2) {
		return x2 >= y1 && y2 >= x1;
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
		return "RectangleBounds{" +
					 "size=" + size +
					 '}';
	}
}
