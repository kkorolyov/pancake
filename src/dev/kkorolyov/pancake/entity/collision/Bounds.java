package dev.kkorolyov.pancake.entity.collision;

/**
 * Defines abstract boundaries with some origin point.
 */
public interface Bounds {
	/**
	 * Checks for intersection between bounds.
	 * @param other bounds against which to check intersection
	 * @return {@code true} if bounds intersect
	 */
	boolean intersects(Bounds other);
	
	/** @return origin point */
	Vector getOrigin();
}
