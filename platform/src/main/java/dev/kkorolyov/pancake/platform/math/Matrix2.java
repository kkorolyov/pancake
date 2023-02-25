package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A 2x2 matrix.
 */
public sealed class Matrix2 permits Matrix3 {
	private double xx, xy, yx, yy;

	/**
	 * Returns a new 2x2 identity matrix.
	 */
	public static Matrix2 identity() {
		return new Matrix2(
				1, 0,
				0, 1
		);
	}

	/**
	 * Returns a new 2x2 matrix for the given configuration.
	 */
	public static Matrix2 of(
			double xx, double xy,
			double yx, double yy
	) {
		return new Matrix2(xx, xy, yx, yy);
	}

	Matrix2(
			double xx, double xy,
			double yx, double yy
	) {
		setXx(xx);
		setXy(xy);
		setYx(yx);
		setYy(yy);
	}

	/**
	 * Scales this matrix by {@code value}.
	 */
	public void scale(double value) {
		setXx(xx * value);
		setXy(xy * value);
		setYx(yx * value);
		setYy(yy * value);
	}

	/**
	 * Adds {@code other}'s components to this matrix.
	 */
	public final void add(Matrix2 other) {
		setXx(xx + other.xx);
		setXy(xy + other.xy);
		setYx(yx + other.yx);
		setYy(yy + other.yy);
	}
	/**
	 * Adds {@code scale} proportion of {@code other}'s components to this matrix.
	 */
	public final void add(Matrix2 other, double scale) {
		setXx(xx + other.xx * scale);
		setXy(xy + other.xy * scale);
		setYx(yx + other.yx * scale);
		setYy(yy + other.yy * scale);
	}

	public final double getXx() {
		return xx;
	}
	public final void setXx(double xx) {
		this.xx = FloatOps.sanitize(xx);
	}

	public final double getXy() {
		return xy;
	}
	public final void setXy(double xy) {
		this.xy = FloatOps.sanitize(xy);
	}

	public final double getYx() {
		return yx;
	}
	public final void setYx(double yx) {
		this.yx = FloatOps.sanitize(yx);
	}

	public final double getYy() {
		return yy;
	}
	public final void setYy(double yy) {
		this.yy = FloatOps.sanitize(yy);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Matrix2 matrix2 = (Matrix2) o;
		return FloatOps.equals(matrix2.xx, xx) && FloatOps.equals(matrix2.xy, xy) && FloatOps.equals(matrix2.yx, yx) && FloatOps.equals(matrix2.yy, yy);
	}
	@Override
	public int hashCode() {
		return Objects.hash(xx, xy, yx, yy);
	}

	@Override
	public String toString() {
		return String.format("((%.9f,%.9f),(%.9f,%.9f))", xx, xy, yx, yy);
	}
}
