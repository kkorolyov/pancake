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

	static double sanitize(double val) {
		return val == 0.0 ? 0 : val;
	}
	static boolean equals(double val, double other) {
		return Math.abs(val - other) < 1e-9;
	}

	Matrix2(
			double xx, double xy,
			double yx, double yy
	) {
		this.xx = xx;
		this.xy = xy;
		this.yx = yx;
		this.yy = yy;
	}

	/**
	 * Scales this matrix by {@code value}.
	 */
	public void scale(double value) {
		xx *= value;
		xy *= value;
		yx *= value;
		yy *= value;
	}

	/**
	 * Adds {@code other}'s components to this matrix.
	 */
	public final void add(Matrix2 other) {
		xx += other.xx;
		xy += other.xy;
		yx += other.yx;
		yy += other.yy;
	}
	/**
	 * Adds {@code scale} proportion of {@code other}'s components to this matrix.
	 */
	public final void add(Matrix2 other, double scale) {
		xx += other.xx * scale;
		xy += other.xy * scale;
		yx += other.yx * scale;
		yy += other.yy * scale;
	}

	public final double getXx() {
		return sanitize(xx);
	}
	public final void setXx(double xx) {
		this.xx = xx;
	}

	public final double getXy() {
		return sanitize(xy);
	}
	public final void setXy(double xy) {
		this.xy = xy;
	}

	public final double getYx() {
		return sanitize(yx);
	}
	public final void setYx(double yx) {
		this.yx = yx;
	}

	public final double getYy() {
		return sanitize(yy);
	}
	public final void setYy(double yy) {
		this.yy = yy;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Matrix2 matrix2 = (Matrix2) o;
		return equals(matrix2.getXx(), getXx()) && equals(matrix2.getXy(), getXy()) && equals(matrix2.getYx(), getYx()) && equals(matrix2.getYy(), getYy());
	}
	@Override
	public int hashCode() {
		return Objects.hash(getXx(), getXy(), getYx(), getYy());
	}
}
