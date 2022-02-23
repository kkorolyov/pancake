package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A 4x4 matrix.
 */
public final class Matrix4 extends Matrix3 {
	private double xw, yw, zw, wx, wy, wz, ww;

	/**
	 * Returns a new 4x4 identity matrix.
	 */
	public static Matrix4 identity() {
		return new Matrix4(
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1
		);
	}

	/**
	 * Returns a new 4x4 matrix for the given configuration.
	 */
	public static Matrix3 of(
			double xx, double xy, double xz, double xw,
			double yx, double yy, double yz, double yw,
			double zx, double zy, double zz, double zw,
			double wx, double wy, double wz, double ww
	) {
		return new Matrix4(xx, xy, xz, xw, yx, yy, yz, yw, zx, zy, zz, zw, wx, wy, wz, ww);
	}

	Matrix4(
			double xx, double xy, double xz, double xw,
			double yx, double yy, double yz, double yw,
			double zx, double zy, double zz, double zw,
			double wx, double wy, double wz, double ww
	) {
		super(xx, xy, xz, yx, yy, yz, zx, zy, zz);
		this.xw = xw;
		this.yw = yw;
		this.zw = zw;
		this.wx = wx;
		this.wy = wy;
		this.wz = wz;
		this.ww = ww;
	}

	@Override
	public void scale(double value) {
		super.scale(value);
	}

	/**
	 * Adds {@code other}'s components to this matrix.
	 */
	public void add(Matrix4 other) {
		add((Matrix3) other);
		xw += other.xw;
		yw += other.yw;
		zw += other.zw;
		wx += other.wx;
		wy += other.wy;
		wz += other.wz;
		ww += other.ww;
	}
	/**
	 * Adds {@code scale} proportion of {@code other}'s components to this matrix.
	 */
	public void add(Matrix4 other, double scale) {
		add((Matrix3) other, scale);
		xw += other.xw * scale;
		yw += other.yw * scale;
		zw += other.zw * scale;
		wx += other.wx * scale;
		wy += other.wy * scale;
		wz += other.wz * scale;
		ww += other.ww * scale;
	}

	public double getXw() {
		return sanitize(xw);
	}
	public void setXw(double xw) {
		this.xw = xw;
	}

	public double getYw() {
		return sanitize(yw);
	}
	public void setYw(double yw) {
		this.yw = yw;
	}

	public double getZw() {
		return sanitize(zw);
	}
	public void setZw(double zw) {
		this.zw = zw;
	}

	public double getWx() {
		return sanitize(wx);
	}
	public void setWx(double wx) {
		this.wx = wx;
	}

	public double getWy() {
		return sanitize(wy);
	}
	public void setWy(double wy) {
		this.wy = wy;
	}

	public double getWz() {
		return sanitize(wz);
	}
	public void setWz(double wz) {
		this.wz = wz;
	}

	public double getWw() {
		return sanitize(ww);
	}
	public void setWw(double ww) {
		this.ww = ww;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Matrix4 matrix4 = (Matrix4) o;
		return equals(matrix4.getXw(), getXw()) && equals(matrix4.getYw(), getYw()) && equals(matrix4.getZw(), getZw()) && equals(matrix4.getWx(), getWx()) && equals(matrix4.getWy(), getWy()) && equals(matrix4.getWz(), getWz()) && equals(matrix4.getWw(), getWw());
	}
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getXw(), getYw(), getZw(), getWx(), getWy(), getWz(), getWw());
	}
}
