package dev.kkorolyov.pancake.platform.math;

/**
 * A head at some point in 3 dimensions and tail at {@code (0, 0, 0)}.
 */
public interface Vector3 extends Vector2 {
	/** @return z component */
	double getZ();
	/** @param value z component */
	void setZ(double value);

	/**
	 * Sets this vector equal to {@code other}.
	 * @param other vector to match
	 */
	void set(Vector3 other);
	/**
	 * Translates the head of this vector by {@code other}.
	 */
	void add(Vector3 other);
	/**
	 * Translates the head of this vector by {@code scale} proportion of {@code other}.
	 */
	void add(Vector3 other, double scale);
}
