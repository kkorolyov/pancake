package dev.kkorolyov.pancake.platform.math;

/**
 * A point in 1-dimensional space.
 */
public interface Vector1 {
	/** @return x component */
	double getX();
	/** @param value x component */
	void setX(double value);

	/**
	 * Scales this vector by {@code value}.
	 */
	void scale(double value);

	/**
	 * Sets this vector equal to {@code other}.
	 * @param other vector to match
	 */
	void set(Vector1 other);
	/**
	 * Translates the head of this vector by {@code other}.
	 */
	void add(Vector1 other);
	/**
	 * Translates the head of this vector by {@code scale} proportion of {@code other}.
	 */
	void add(Vector1 other, double scale);
}
