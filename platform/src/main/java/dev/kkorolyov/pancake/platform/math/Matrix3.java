package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A 3x3 matrix.
 */
public sealed class Matrix3 extends Matrix2 permits Matrix4 {
	private double xz, yz, zx, zy, zz;

	/**
	 * Returns a new 3x3 identity matrix.
	 */
	public static Matrix3 identity() {
		return new Matrix3(
				1, 0, 0,
				0, 1, 0,
				0, 0, 1
		);
	}

	/**
	 * Returns a new 3x3 matrix for the given configuration.
	 */
	public static Matrix3 of(
			double xx, double xy, double xz,
			double yx, double yy, double yz,
			double zx, double zy, double zz
	) {
		return new Matrix3(xx, xy, xz, yx, yy, yz, zx, zy, zz);
	}

	Matrix3(
			double xx, double xy, double xz,
			double yx, double yy, double yz,
			double zx, double zy, double zz
	) {
		super(xx, xy, yx, yy);
		this.xz = xz;
		this.yz = yz;
		this.zx = zx;
		this.zy = zy;
		this.zz = zz;
	}

	@Override
	public void scale(double value) {
		super.scale(value);
		xz *= value;
		yz *= value;
		zx *= value;
		zy *= value;
		zz *= value;
	}

	/**
	 * Adds {@code other}'s components to this matrix.
	 */
	public final void add(Matrix3 other) {
		add((Matrix2) other);
		xz += other.xz;
		yz += other.yz;
		zx += other.zx;
		zy += other.zy;
		zz += other.zz;
	}
	/**
	 * Adds {@code scale} proportion of {@code other}'s components to this matrix.
	 */
	public final void add(Matrix3 other, double scale) {
		add((Matrix2) other, scale);
		xz += other.xz * scale;
		yz += other.yz * scale;
		zx += other.zx * scale;
		zy += other.zy * scale;
		zz += other.zz * scale;
	}

	public final double getXz() {
		return FloatOps.sanitize(xz);
	}
	public final void setXz(double xz) {
		this.xz = xz;
	}

	public final double getYz() {
		return FloatOps.sanitize(yz);
	}
	public final void setYz(double yz) {
		this.yz = yz;
	}

	public final double getZx() {
		return FloatOps.sanitize(zx);
	}
	public final void setZx(double zx) {
		this.zx = zx;
	}

	public final double getZy() {
		return FloatOps.sanitize(zy);
	}
	public final void setZy(double zy) {
		this.zy = zy;
	}

	public final double getZz() {
		return FloatOps.sanitize(zz);
	}
	public final void setZz(double zz) {
		this.zz = zz;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Matrix3 matrix3 = (Matrix3) o;
		return FloatOps.equals(matrix3.getXz(), getXz()) && FloatOps.equals(matrix3.getYz(), getYz()) && FloatOps.equals(matrix3.getZx(), getZx()) && FloatOps.equals(matrix3.getZy(), getZy()) && FloatOps.equals(matrix3.getZz(), getZz());
	}
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getXz(), getYz(), getZx(), getZy(), getZz());
	}

	@Override
	public String toString() {
		return String.format("((%.9f,%.9f,%.9f),(%.9f,%.9f,%.9f),(%.9f,%.9f,%.9f))", getXx(), getXy(), xz, getYx(), getYy(), yz, zx, zy, zz);
	}
}
