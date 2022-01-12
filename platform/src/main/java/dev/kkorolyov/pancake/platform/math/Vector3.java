package dev.kkorolyov.pancake.platform.math;

/**
 * A head at some point in 3 dimensions and tail at {@code (0, 0, 0)}.
 */
public interface Vector3 extends Vector2 {
	/**
	 * Returns the length of {@code vector}.
	 */
	static double magnitude(Vector3 vector) {
		return Math.sqrt(dot(vector, vector));
	}
	/**
	 * Returns the dot product of {@code a} and {@code b}.
	 */
	static double dot(Vector3 a, Vector3 b) {
		return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
	}
	/**
	 * Returns the Euclidean distance between {@code a} and {@code b}.
	 */
	static double distance(Vector3 a, Vector3 b) {
		return Math.sqrt(
				Math.pow(a.getX() - b.getX(), 2) +
						Math.pow(a.getY() - b.getY(), 2) +
						Math.pow(a.getZ() - b.getZ(), 2)
		);
	}

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

	/**
	 * Resizes this vector to length {@code 1}.
	 */
	@Override
	default void normalize() {
		double magnitude = magnitude(this);
		scale(magnitude == 0 ? 0 : 1 / magnitude);
	}
	/**
	 * Projects this vector along {@code other}.
	 */
	default void project(Vector3 other) {
		scale(dot(this, other) / dot(other, other));
	}
	/**
	 * Reflects this vector along {@code other}.
	 */
	default void reflect(Vector3 other) {
		add(other, 2 * dot(this, other));
	}
}
