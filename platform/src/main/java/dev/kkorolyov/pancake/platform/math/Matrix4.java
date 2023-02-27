package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A 4x4 matrix.
 */
public final class Matrix4 extends Matrix3 {
	// reusable 2nd matrix for transformations
	private static final ThreadLocal<Matrix4> tOp = ThreadLocal.withInitial(Matrix4::identity);

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
	public static Matrix4 of(
			double xx, double xy, double xz, double xw,
			double yx, double yy, double yz, double yw,
			double zx, double zy, double zz, double zw,
			double wx, double wy, double wz, double ww
	) {
		return new Matrix4(xx, xy, xz, xw, yx, yy, yz, yw, zx, zy, zz, zw, wx, wy, wz, ww);
	}

	private static Matrix4 initOp() {
		Matrix4 op = tOp.get();
		op.reset();
		return op;
	}

	Matrix4(
			double xx, double xy, double xz, double xw,
			double yx, double yy, double yz, double yw,
			double zx, double zy, double zz, double zw,
			double wx, double wy, double wz, double ww
	) {
		super(xx, xy, xz, yx, yy, yz, zx, zy, zz);
		setXw(xw);
		setYw(yw);
		setZw(zw);
		setWx(wx);
		setWy(wy);
		setWz(wz);
		setWw(ww);
	}

	@Override
	public void scale(double value) {
		super.scale(value);
		setXw(xw * value);
		setYw(yw * value);
		setZw(zw * value);
		setWx(wx * value);
		setWy(wy * value);
		setWz(wz * value);
		setWw(ww * value);
	}

	/**
	 * Adds {@code other}'s components to this matrix.
	 */
	public void add(Matrix4 other) {
		add((Matrix3) other);
		setXw(xw + other.xw);
		setYw(yw + other.yw);
		setZw(zw + other.zw);
		setWx(wx + other.wx);
		setWy(wy + other.wy);
		setWz(wz + other.wz);
		setWw(ww + other.ww);
	}
	/**
	 * Adds {@code scale} proportion of {@code other}'s components to this matrix.
	 */
	public void add(Matrix4 other, double scale) {
		add((Matrix3) other, scale);
		setXw(xw + other.xw * scale);
		setYw(yw + other.yw * scale);
		setZw(zw + other.zw * scale);
		setWx(wx + other.wx * scale);
		setWy(wy + other.wy * scale);
		setWz(wz + other.wz * scale);
		setWw(ww + other.ww * scale);
	}

	/**
	 * Matrix-multiplies this matrix with {@code other} and sets the result on this matrix.
	 */
	public void multiply(Matrix4 other) {
		double newXx = getXx() * other.getXx() + getXy() * other.getYx() + getXz() * other.getZx() + xw * other.wx;
		double newXy = getXx() * other.getXy() + getXy() * other.getYy() + getXz() * other.getZy() + xw * other.wy;
		double newXz = getXx() * other.getXz() + getXy() * other.getYz() + getXz() * other.getZz() + xw * other.wz;
		double newXw = getXx() * other.xw + getXy() * other.yw + getXz() * other.zw + xw * other.ww;

		double newYx = getYx() * other.getXx() + getYy() * other.getYx() + getYz() * other.getZx() + yw * other.wx;
		double newYy = getYx() * other.getXy() + getYy() * other.getYy() + getYz() * other.getZy() + yw * other.wy;
		double newYz = getYx() * other.getXz() + getYy() * other.getYz() + getYz() * other.getZz() + yw * other.wz;
		double newYw = getYx() * other.xw + getYy() * other.yw + getYz() * other.zw + yw * other.ww;

		double newZx = getZx() * other.getXx() + getZy() * other.getYx() + getZz() * other.getZx() + zw * other.wx;
		double newZy = getZx() * other.getXy() + getZy() * other.getYy() + getZz() * other.getZy() + zw * other.wy;
		double newZz = getZx() * other.getXz() + getZy() * other.getYz() + getZz() * other.getZz() + zw * other.wz;
		double newZw = getZx() * other.xw + getZy() * other.yw + getZz() * other.zw + zw * other.ww;

		double newWx = getWx() * other.getXx() + getWy() * other.getYx() + getWz() * other.getZx() + ww * other.wx;
		double newWy = getWx() * other.getXy() + getWy() * other.getYy() + getWz() * other.getZy() + ww * other.wy;
		double newWz = getWx() * other.getXz() + getWy() * other.getYz() + getWz() * other.getZz() + ww * other.wz;
		double newWw = getWx() * other.xw + getWy() * other.yw + getWz() * other.zw + ww * other.ww;

		setXx(newXx);
		setXy(newXy);
		setXz(newXz);
		setXw(newXw);
		setYx(newYx);
		setYy(newYy);
		setYz(newYz);
		setYw(newYw);
		setZx(newZx);
		setZy(newZy);
		setZz(newZz);
		setZw(newZw);
		setWx(newWx);
		setWy(newWy);
		setWz(newWz);
		setWw(newWw);
	}

	/**
	 * Applies {@code translation} to this matrix.
	 * Intended for building a transformation matrix from an {@link #identity()} matrix.
	 * Should be applied before rotation and scaling.
	 * @see #rotate(double, Vector3)
	 * @see #scale(Vector3)
	 */
	public void translate(Vector3 translation) {
		Matrix4 op = initOp();

		op.setXw(xw + translation.getX());
		op.setYw(yw + translation.getY());
		op.setZw(zw + translation.getZ());

		multiply(op);
	}
	/**
	 * Applies a rotation of {@code radians} about {@code axis} on this matrix.
	 * Intended for building a transformation matrix from an {@link #identity()} matrix.
	 * Should be applied after translation but before scaling.
	 * @see #scale(Vector3)
	 * @see #translate(Vector3)
	 */
	public void rotate(double radians, Vector3 axis) {
		double cosTheta = Math.cos(radians);
		double sinTheta = Math.sin(radians);
		double iCosTheta = 1 - cosTheta;

		Matrix4 op = initOp();

		op.setXx(cosTheta + axis.getX() * axis.getX() * iCosTheta);
		op.setXy(axis.getX() * axis.getY() * iCosTheta - axis.getZ() * sinTheta);
		op.setXz(axis.getX() * axis.getZ() * iCosTheta + axis.getY() * sinTheta);
		op.setYx(axis.getY() * axis.getX() * iCosTheta + axis.getZ() * sinTheta);
		op.setYy(cosTheta + axis.getY() * axis.getY() * iCosTheta);
		op.setYz(axis.getY() * axis.getZ() * iCosTheta - axis.getX() * sinTheta);
		op.setZx(axis.getZ() * axis.getX() * iCosTheta - axis.getY() * sinTheta);
		op.setZy(axis.getZ() * axis.getY() * iCosTheta + axis.getX() * sinTheta);
		op.setZz(cosTheta + axis.getZ() * axis.getZ() * iCosTheta);

		multiply(op);
	}
	/**
	 * Applies {@code scale} to this matrix.
	 * Intended for building a transformation matrix from an {@link #identity()} matrix.
	 * Should be applied after translation and rotation.
	 * @see #translate(Vector3)
	 * @see #rotate(double, Vector3)
	 */
	public void scale(Vector3 scale) {
		Matrix4 op = initOp();

		op.setXx(scale.getX());
		op.setYy(scale.getY());
		op.setZz(scale.getZ());

		multiply(op);
	}

	/**
	 * Sets this matrix to the {@link #identity()} matrix.
	 */
	public void reset() {
		setXx(1);
		setXy(0);
		setXz(0);
		setXw(0);
		setYx(0);
		setYy(1);
		setYz(0);
		setYw(0);
		setZx(0);
		setZy(0);
		setZz(1);
		setZw(0);
		setWx(0);
		setWy(0);
		setWz(0);
		setWw(1);
	}

	public double getXw() {
		return xw;
	}
	public void setXw(double xw) {
		this.xw = FloatOps.sanitize(xw);
	}

	public double getYw() {
		return yw;
	}
	public void setYw(double yw) {
		this.yw = FloatOps.sanitize(yw);
	}

	public double getZw() {
		return zw;
	}
	public void setZw(double zw) {
		this.zw = FloatOps.sanitize(zw);
	}

	public double getWx() {
		return wx;
	}
	public void setWx(double wx) {
		this.wx = FloatOps.sanitize(wx);
	}

	public double getWy() {
		return wy;
	}
	public void setWy(double wy) {
		this.wy = FloatOps.sanitize(wy);
	}

	public double getWz() {
		return wz;
	}
	public void setWz(double wz) {
		this.wz = FloatOps.sanitize(wz);
	}

	public double getWw() {
		return ww;
	}
	public void setWw(double ww) {
		this.ww = FloatOps.sanitize(ww);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Matrix4 matrix4 = (Matrix4) o;
		return FloatOps.equals(matrix4.xw, xw) && FloatOps.equals(matrix4.yw, yw) && FloatOps.equals(matrix4.zw, zw) && FloatOps.equals(matrix4.wx, wx) && FloatOps.equals(matrix4.wy, wy) && FloatOps.equals(matrix4.wz, wz) && FloatOps.equals(matrix4.ww, ww);
	}
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), xw, yw, zw, wx, wy, wz, ww);
	}

	@Override
	public String toString() {
		return String.format("((%.9f,%.9f,%.9f,%.9f),(%.9f,%.9f,%.9f,%.9f),(%.9f,%.9f,%.9f,%.9f),(%.9f,%.9f,%.9f,%.9f))", getXx(), getXy(), getXz(), xw, getYx(), getYy(), getYz(), yw, getZx(), getZy(), getZz(), zw, wx, wy, wz, ww);
	}
}
