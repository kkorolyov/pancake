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
		return of(other.x);
	}

	/**
	 * Returns a 1-dimensional vector initialized to {@code (0)}.
	 */
	public static Vector1 of() {
		return of(0);
	}
	/**
	 * Returns a 1-dimensional vector initialized to {@code (x)}.
	 */
	public static Vector1 of(double x) {
		return new Vector1(x);
	}

	Vector1(double x) {
		setX(x);
	}

	/**
	 * Scales this vector by {@code value}.
	 */
	public void scale(double value) {
		setX(x * value);
	}

	/**
	 * Sets this vector equal to {@code other}.
	 * @param other vector to match
	 */
	public final void set(Vector1 other) {
		setX(other.x);
	}
	/**
	 * Translates the head of this vector by {@code other}.
	 */
	public final void add(Vector1 other) {
		setX(x + other.x);
	}
	/**
	 * Translates the head of this vector by {@code scale} proportion of {@code other}.
	 */
	public final void add(Vector1 other, double scale) {
		setX(x + other.x * scale);
	}

	public final double getX() {
		return x;
	}
	public final void setX(double x) {
		this.x = FloatOps.sanitize(x);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Vector1 o = (Vector1) obj;
		return FloatOps.equals(o.x, x);
	}
	@Override
	public int hashCode() {
		return Objects.hash(x);
	}

	@Override
	public String toString() {
		return String.format("(%.9f)", x);
	}
}
