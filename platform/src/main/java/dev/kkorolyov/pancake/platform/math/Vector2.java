package dev.kkorolyov.pancake.platform.math;

/**
 * A head at some point in 2 dimensions and tail at {@code (0, 0)}.
 */
public interface Vector2 extends Vector1 {
	/**
	 * Returns the dot product of {@code a} and {@code b}.
	 */
	static double dot(Vector2 a, Vector2 b) {
		return a.getX() * b.getX() + a.getY() * b.getY();
	}
	/**
	 * Returns the length of {@code vector}.
	 */
	static double magnitude(Vector2 vector) {
		return Math.sqrt(dot(vector, vector));
	}
	/**
	 * Returns the Euclidean distance between {@code a} and {@code b}.
	 */
	static double distance(Vector2 a, Vector2 b) {
		return Math.sqrt(
				Math.pow(a.getX() - b.getX(), 2) +
						Math.pow(a.getY() - b.getY(), 2)
		);
	}

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

	/**
	 * Resizes this vector to length {@code 1}.
	 */
	default void normalize() {
		double magnitude = magnitude(this);
		scale(magnitude == 0 ? 0 : 1 / magnitude);
	}
	/**
	 * Morphs this vector to an orthogonal representation.
	 */
	default void orthogonal() {
		double temp = getX();
		setX(-getY());
		setY(temp);
	}
	/**
	 * Projects this vector along {@code other}.
	 */
	default void project(Vector2 other) {
		double scale = dot(this, other) / dot(other, other);
		set(other);
		scale(scale);
	}
	/**
	 * Reflects this vector along {@code other}.
	 */
	default void reflect(Vector2 other) {
		add(other, -2 * dot(this, other));
	}
}
