package dev.kkorolyov.pancake.platform.math;

/**
 * A head at some point in 2 dimensions and tail at {@code (0, 0)}.
 */
public interface Vector2 extends Vector1 {
	/** @return y component */
	double getY();
	/** @param value y component */
	void setY(double value);

	/**
	 * Sets this vector equal to {@code other}.
	 * @param other vector to match
	 */
	void set(Vector2 other);
	/**
	 * Translates the head of this vector by {@code other}.
	 */
	void add(Vector2 other);
	/**
	 * Translates the head of this vector by {@code scale} proportion of {@code other}.
	 */
	void add(Vector2 other, double scale);
}
