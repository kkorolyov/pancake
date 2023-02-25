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
		setXz(xz);
		setYz(yz);
		setZx(zx);
		setZy(zy);
		setZz(zz);
	}

	@Override
	public void scale(double value) {
		super.scale(value);
		setXz(xz * value);
		setYz(yz * value);
		setZx(zx * value);
		setZy(zy * value);
		setZz(zz * value);
	}

	/**
	 * Adds {@code other}'s components to this matrix.
	 */
	public final void add(Matrix3 other) {
		add((Matrix2) other);
		setXz(xz + other.xz);
		setYz(yz + other.yz);
		setZx(zx + other.zx);
		setZy(zy + other.zy);
		setZz(zz + other.zz);
	}
	/**
	 * Adds {@code scale} proportion of {@code other}'s components to this matrix.
	 */
	public final void add(Matrix3 other, double scale) {
		add((Matrix2) other, scale);
		setXz(xz + other.xz * scale);
		setYz(yz + other.yz * scale);
		setZx(zx + other.zx * scale);
		setZy(zy + other.zy * scale);
		setZz(zz + other.zz * scale);
	}

	public final double getXz() {
		return xz;
	}
	public final void setXz(double xz) {
		this.xz = FloatOps.sanitize(xz);
	}

	public final double getYz() {
		return yz;
	}
	public final void setYz(double yz) {
		this.yz = FloatOps.sanitize(yz);
	}

	public final double getZx() {
		return zx;
	}
	public final void setZx(double zx) {
		this.zx = FloatOps.sanitize(zx);
	}

	public final double getZy() {
		return zy;
	}
	public final void setZy(double zy) {
		this.zy = FloatOps.sanitize(zy);
	}

	public final double getZz() {
		return zz;
	}
	public final void setZz(double zz) {
		this.zz = FloatOps.sanitize(zz);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Matrix3 matrix3 = (Matrix3) o;
		return FloatOps.equals(matrix3.xz, xz) && FloatOps.equals(matrix3.yz, yz) && FloatOps.equals(matrix3.zx, zx) && FloatOps.equals(matrix3.zy, zy) && FloatOps.equals(matrix3.zz, zz);
	}
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), xz, yz, zx, zy, zz);
	}

	@Override
	public String toString() {
		return String.format("((%.9f,%.9f,%.9f),(%.9f,%.9f,%.9f),(%.9f,%.9f,%.9f))", getXx(), getXy(), xz, getYx(), getYy(), yz, zx, zy, zz);
	}
}
