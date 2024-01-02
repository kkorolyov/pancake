package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A head at some point in 2 dimensions and tail at {@code (0, 0)}.
 */
public interface Vector2 {
	/**
	 * Returns the length of {@code vector}.
	 */
	static double magnitude(Vector2 vector) {
		return FloatOps.sanitize(Math.sqrt(dot(vector, vector)));
	}
	/**
	 * Returns the dot product of {@code a} and {@code b}.
	 */
	static double dot(Vector2 a, Vector2 b) {
		return FloatOps.sanitize(a.getX() * b.getX() + a.getY() * b.getY());
	}
	/**
	 * Returns the Euclidean distance between {@code a} and {@code b}.
	 */
	static double distance(Vector2 a, Vector2 b) {
		return FloatOps.sanitize(
				Math.sqrt(
						Math.pow(a.getX() - b.getX(), 2) +
								Math.pow(a.getY() - b.getY(), 2)
				)
		);
	}

	/**
	 * Returns a 2-dimensional vector initialized to {@code other}.
	 */
	static Vector2 of(Vector2 other) {
		return of(other.getX(), other.getY());
	}

	/**
	 * Returns a 2-dimensional vector initialized to {@code (0, 0)}.
	 */
	static Vector2 of() {
		return of(0);
	}
	/**
	 * Returns a 2-dimensional vector initialized to {@code (x, 0)}.
	 */
	static Vector2 of(double x) {
		return of(x, 0);
	}
	/**
	 * Returns a 2-dimensional vector initialized to {@code (x, y)}.
	 */
	static Vector2 of(double x, double y) {
		return new Vector2.Value(x, y);
	}

	/**
	 * Generic {@link Object#equals(Object)} implementation for {@code Vector2} instances.
	 * Uses {@code instanceof}, rather than a class equality check.
	 */
	static boolean equals(Vector2 vector, Object obj) {
		if (vector == obj) return true;
		if (!(obj instanceof Vector2 o)) return false;
		return FloatOps.equals(vector.getX(), o.getX()) && FloatOps.equals(vector.getY(), o.getY());
	}
	/**
	 * Generic {@link Object#hashCode()} implementation for {@code Vector2} instances.
	 */
	static int hashCode(Vector2 vector) {
		return Objects.hash(vector.getX(), vector.getY());
	}
	/**
	 * Generic {@link Object#toString()} implementation for {@code Vector2} instances.
	 */
	static String toString(Vector2 vector) {
		return String.format("(%.9f,%.9f)", vector.getX(), vector.getY());
	}

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
		add(other, -2 * dot(this, other) / dot(other, other));
	}

	/**
	 * Scales this vector by {@code value}.
	 */
	default void scale(double value) {
		setX(getX() * value);
		setY(getY() * value);
	}

	/**
	 * Sets this vector equal to {@code other}.
	 * @param other vector to match
	 */
	default void set(Vector2 other) {
		setX(other.getX());
		setY(other.getY());
	}
	/**
	 * Translates the head of this vector by {@code other}.
	 */
	default void add(Vector2 other) {
		setX(getX() + other.getX());
		setY(getY() + other.getY());
	}
	/**
	 * Translates the head of this vector by {@code scale} proportion of {@code other}.
	 */
	default void add(Vector2 other, double scale) {
		setX(getX() + other.getX() * scale);
		setY(getY() + other.getY() * scale);
	}

	/**
	 * Sets this vector to all {@code 0}.
	 */
	default void reset() {
		setX(0);
		setY(0);
	}

	double getX();
	void setX(double x);

	double getY();
	void setY(double y);

	/**
	 * Basic mutable value-based {@code Vector2} implementation.
	 */
	sealed class Value implements Vector2 permits Vector3.Value {
		private double x;
		private double y;

		Value(double x, double y) {
			setX(x);
			setY(y);
		}

		public double getX() {
			return x;
		}
		public void setX(double x) {
			this.x = FloatOps.sanitize(x);
		}

		public double getY() {
			return y;
		}
		public void setY(double y) {
			this.y = FloatOps.sanitize(y);
		}

		@Override
		public boolean equals(Object obj) {
			return Vector2.equals(this, obj);
		}
		@Override
		public int hashCode() {
			return Vector2.hashCode(this);
		}

		@Override
		public String toString() {
			return Vector2.toString(this);
		}
	}
}
