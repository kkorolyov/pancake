package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A head at some point in 2 dimensions and tail at {@code (0, 0)}.
 */
public sealed class Vector2 extends Vector1 permits Vector3 {
	private double y;

	/**
	 * Returns the length of {@code vector}.
	 */
	public static double magnitude(Vector2 vector) {
		return FloatOps.sanitize(Math.sqrt(dot(vector, vector)));
	}
	/**
	 * Returns the dot product of {@code a} and {@code b}.
	 */
	public static double dot(Vector2 a, Vector2 b) {
		return FloatOps.sanitize(a.getX() * b.getX() + a.y * b.y);
	}
	/**
	 * Returns the Euclidean distance between {@code a} and {@code b}.
	 */
	public static double distance(Vector2 a, Vector2 b) {
		return FloatOps.sanitize(
				Math.sqrt(
						Math.pow(a.getX() - b.getX(), 2) +
								Math.pow(a.y - b.y, 2)
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
		return of(other.getX(), other.y);
	}

	/**
	 * Returns a 2-dimensional vector initialized to {@code (0, 0)}.
	 */
	public static Vector2 of() {
		return of(0);
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
		setY(y);
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
		setX(-y);
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
	public final void reflect(Vector2 other) {
		add(other, -2 * dot(this, other) / dot(other, other));
	}

	@Override
	public void scale(double value) {
		super.scale(value);
		setY(y * value);
	}

	/**
	 * Sets this vector equal to {@code other}.
	 * @param other vector to match
	 */
	public final void set(Vector2 other) {
		set((Vector1) other);
		setY(other.y);
	}
	/**
	 * Translates the head of this vector by {@code other}.
	 */
	public final void add(Vector2 other) {
		add((Vector1) other);
		setY(y + other.y);
	}
	/**
	 * Translates the head of this vector by {@code scale} proportion of {@code other}.
	 */
	public final void add(Vector2 other, double scale) {
		add((Vector1) other, scale);
		setY(y + other.y * scale);
	}

	/**
	 * Sets this vector to all {@code 0}.
	 */
	public void reset() {
		setX(0);
		setY(0);
	}

	public final double getY() {
		return y;
	}
	public final void setY(double y) {
		this.y = FloatOps.sanitize(y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		if (!super.equals(obj)) return false;
		Vector2 o = (Vector2) obj;
		return FloatOps.equals(o.y, y);
	}
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), y);
	}

	@Override
	public String toString() {
		return String.format("(%.9f,%.9f)", getX(), y);
	}
}
