package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A head at some point in 2 dimensions and tail at {@code (0, 0)}.
 */
public sealed class Vector2 extends Vector1 permits Vector3 {
	private double y;

	/**
	 * Returns the dot product of {@code a} and {@code b}.
	 */
	public static double dot(Vector2 a, Vector2 b) {
		return FloatOps.sanitize(a.getX() * b.getX() + a.getY() * b.getY());
	}
	/**
	 * Returns the length of {@code vector}.
	 */
	public static double magnitude(Vector2 vector) {
		return FloatOps.sanitize(Math.sqrt(dot(vector, vector)));
	}
	/**
	 * Returns the Euclidean distance between {@code a} and {@code b}.
	 */
	public static double distance(Vector2 a, Vector2 b) {
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
	public static Vector2 of(Vector1 other) {
		return of(other.getX());
	}
	/**
	 * Returns a 2-dimensional vector initialized to {@code other}.
	 */
	public static Vector2 of(Vector2 other) {
		return of(other.getX(), other.getY());
	}

	/**
	 * Returns a 2-dimensional vector initialized to {@code (x, 0)}.
	 */
	public static Vector2 of(double x) {
		return of(x, 0);
	}
	/**
	 * Returns a 2-dimensional vector initialized to {@code (x, y)}.
	 */
	public static Vector2 of(double x, double y) {
		return new Vector2(x, y);
	}

	Vector2(double x, double y) {
		super(x);
		this.y = y;
	}

	/**
	 * Resizes this vector to length {@code 1}.
	 */
	public void normalize() {
		double magnitude = magnitude(this);
		scale(magnitude == 0 ? 0 : 1 / magnitude);
	}
	/**
	 * Morphs this vector to an orthogonal representation.
	 */
	public void orthogonal() {
		double temp = getX();
		setX(-getY());
		setY(temp);
	}
	/**
	 * Projects this vector along {@code other}.
	 */
	public void project(Vector2 other) {
		double scale = dot(this, other) / dot(other, other);
		set(other);
		scale(scale);
	}
	/**
	 * Reflects this vector along {@code other}.
	 */
	public void reflect(Vector2 other) {
		add(other, -2 * dot(this, other));
	}

	/** @return y component */
	public double getY() {
		return FloatOps.sanitize(y);
	}
	/** @param value y component */
	public void setY(double value) {
		y = value;
	}

	@Override
	public void scale(double value) {
		super.scale(value);
		y *= value;
	}

	/**
	 * Sets this vector equal to {@code other}.
	 * @param other vector to match
	 */
	public void set(Vector2 other) {
		set((Vector1) other);
		y = other.getY();
	}
	/**
	 * Translates the head of this vector by {@code other}.
	 */
	public void add(Vector2 other) {
		add((Vector1) other);
		y += other.getY();
	}
	/**
	 * Translates the head of this vector by {@code scale} proportion of {@code other}.
	 */
	public void add(Vector2 other, double scale) {
		add((Vector1) other, scale);
		y += other.getY() * scale;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		if (!super.equals(obj)) return false;
		Vector2 o = (Vector2) obj;
		return FloatOps.equals(o.getY(), getY());
	}
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getY());
	}

	@Override
	public String toString() {
		return "(" + String.join(",", String.valueOf(getX()), String.valueOf(getY())) + ")";
	}
}
