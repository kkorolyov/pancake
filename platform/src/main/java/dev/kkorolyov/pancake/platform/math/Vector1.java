package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A point in 1-dimensional space.
 */
public sealed class Vector1 permits Vector2 {
	private double x;

	/**
	 * Returns a 1-dimensional vector initialized to {@code other}.
	 */
	public static Vector1 of(Vector1 other) {
		return of(other.getX());
	}
	/**
	 * Returns a 1-dimensional vector initialized to {@code (x)}.
	 */
	public static Vector1 of(double x) {
		return new Vector1(x);
	}

	Vector1(double x) {
		this.x = x;
	}

	/** @return x component */
	public double getX() {
		return FloatOps.sanitize(x);
	}
	/** @param value x component */
	public void setX(double value) {
		x = value;
	}

	/**
	 * Scales this vector by {@code value}.
	 */
	public void scale(double value) {
		x *= value;
	}

	/**
	 * Sets this vector equal to {@code other}.
	 * @param other vector to match
	 */
	public void set(Vector1 other) {
		x = other.getX();
	}
	/**
	 * Translates the head of this vector by {@code other}.
	 */
	public void add(Vector1 other) {
		x += other.getX();
	}
	/**
	 * Translates the head of this vector by {@code scale} proportion of {@code other}.
	 */
	public void add(Vector1 other, double scale) {
		x += other.getX() * scale;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Vector1 o = (Vector1) obj;
		return FloatOps.equals(o.getX(), getX());
	}
	@Override
	public int hashCode() {
		return Objects.hash(getX());
	}

	@Override
	public String toString() {
		return "(" + getX() + ")";
	}
}
