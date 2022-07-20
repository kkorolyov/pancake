package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A head at some point in 3 dimensions and tail at {@code (0, 0, 0)}.
 */
public final class Vector3 extends Vector2 {
	private double z;

	/**
	 * Returns the dot product of {@code a} and {@code b}.
	 */
	public static double dot(Vector3 a, Vector3 b) {
		return FloatOps.sanitize(a.getX() * b.getX() + a.getY() * b.getY() + a.z * b.z);
	}
	/**
	 * Returns the length of {@code vector}.
	 */
	public static double magnitude(Vector3 vector) {
		return FloatOps.sanitize(Math.sqrt(dot(vector, vector)));
	}
	/**
	 * Returns the Euclidean distance between {@code a} and {@code b}.
	 */
	public static double distance(Vector3 a, Vector3 b) {
		return FloatOps.sanitize(
				Math.sqrt(
						Math.pow(a.getX() - b.getX(), 2) +
								Math.pow(a.getY() - b.getY(), 2) +
								Math.pow(a.z - b.z, 2)
				)
		);
	}

	/**
	 * Returns a 3-dimensional vector initialized to {@code other}.
	 */
	public static Vector3 of(Vector1 other) {
		return of(other.getX());
	}
	/**
	 * Returns a 3-dimensional vector initialized to {@code other}.
	 */
	public static Vector3 of(Vector2 other) {
		return of(other.getX(), other.getY());
	}
	/**
	 * Returns a 3-dimensional vector initialized to {@code other}.
	 */
	public static Vector3 of(Vector3 other) {
		return of(other.getX(), other.getY(), other.z);
	}

	/**
	 * Returns a 3-dimensional vector initialized to {@code (0, 0, 0)}.
	 */
	public static Vector3 of() {
		return of(0);
	}
	/**
	 * Returns a 3-dimensional vector initialized to {@code (x, 0, 0)}.
	 */
	public static Vector3 of(double x) {
		return of(x, 0);
	}
	/**
	 * Returns a 3-dimensional vector initialized to {@code (x, y, 0)}.
	 */
	public static Vector3 of(double x, double y) {
		return of(x, y, 0);
	}
	/**
	 * Returns a 3-dimensional vector initialized to {@code (x, y, z)}.
	 */
	public static Vector3 of(double x, double y, double z) {
		return new Vector3(x, y, z);
	}

	private Vector3(double x, double y, double z) {
		super(x, y);
		setZ(z);
	}

	/**
	 * Resizes this vector to length {@code 1}.
	 */
	@Override
	public void normalize() {
		double magnitude = magnitude(this);
		scale(magnitude == 0 ? 0 : 1 / magnitude);
	}
	/**
	 * Projects this vector along {@code other}.
	 */
	public void project(Vector3 other) {
		double scale = dot(this, other) / dot(other, other);
		set(other);
		scale(scale);
	}
	/**
	 * Reflects this vector along {@code other}.
	 */
	public void reflect(Vector3 other) {
		add(other, -2 * dot(this, other));
	}

	@Override
	public void scale(double value) {
		super.scale(value);
		setZ(z * value);
	}

	/**
	 * Sets this vector equal to {@code other}.
	 * @param other vector to match
	 */
	public void set(Vector3 other) {
		set((Vector2) other);
		setZ(other.z);
	}
	/**
	 * Translates the head of this vector by {@code other}.
	 */
	public void add(Vector3 other) {
		add((Vector2) other);
		setZ(z + other.z);
	}
	/**
	 * Translates the head of this vector by {@code scale} proportion of {@code other}.
	 */
	public void add(Vector3 other, double scale) {
		add((Vector2) other, scale);
		setZ(z + other.z * scale);
	}

	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = FloatOps.sanitize(z);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		if (!super.equals(obj)) return false;
		Vector3 o = (Vector3) obj;
		return FloatOps.equals(o.z, z);
	}
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), z);
	}

	@Override
	public String toString() {
		return "(" + String.join(",", String.valueOf(getX()), String.valueOf(getY()), String.valueOf(z)) + ")";
	}
}
