package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A 4x4 matrix.
 */
public interface Matrix4 extends Matrix3 {
	/**
	 * Returns the determinant of {@code matrix}.
	 */
	static double determinant(Matrix4 matrix) {
		return matrix.getXw() * matrix.getYz() * matrix.getZy() * matrix.getWx()
				- matrix.getXz() * matrix.getYw() * matrix.getZy() * matrix.getWx()
				- matrix.getXw() * matrix.getYy() * matrix.getZz() * matrix.getWx()
				+ matrix.getXy() * matrix.getYw() * matrix.getZz() * matrix.getWx()
				+ matrix.getXw() * matrix.getYy() * matrix.getZw() * matrix.getWx()
				- matrix.getXy() * matrix.getYz() * matrix.getZw() * matrix.getWx()
				- matrix.getXw() * matrix.getYz() * matrix.getZw() * matrix.getWy()
				+ matrix.getXz() * matrix.getYw() * matrix.getZx() * matrix.getWy()
				+ matrix.getXw() * matrix.getYx() * matrix.getZz() * matrix.getWy()
				- matrix.getXx() * matrix.getYw() * matrix.getZz() * matrix.getWy()
				- matrix.getXz() * matrix.getYx() * matrix.getZw() * matrix.getWy()
				+ matrix.getXx() * matrix.getYz() * matrix.getZw() * matrix.getWy()
				+ matrix.getXw() * matrix.getYy() * matrix.getZx() * matrix.getWz()
				- matrix.getXy() * matrix.getYw() * matrix.getZx() * matrix.getWz()
				- matrix.getXw() * matrix.getYx() * matrix.getZy() * matrix.getWz()
				+ matrix.getXx() * matrix.getYw() * matrix.getZy() * matrix.getWz()
				+ matrix.getXy() * matrix.getYx() * matrix.getZw() * matrix.getWz()
				- matrix.getXx() * matrix.getYy() * matrix.getZw() * matrix.getWz()
				- matrix.getXz() * matrix.getYy() * matrix.getZx() * matrix.getWw()
				+ matrix.getXy() * matrix.getYz() * matrix.getZx() * matrix.getWw()
				+ matrix.getXz() * matrix.getYx() * matrix.getZy() * matrix.getWw()
				- matrix.getXx() * matrix.getYz() * matrix.getZy() * matrix.getWw()
				- matrix.getXy() * matrix.getYx() * matrix.getZz() * matrix.getWw()
				+ matrix.getXx() * matrix.getYy() * matrix.getZz() * matrix.getWw();
	}

	/**
	 * Returns a new 4x4 identity matrix.
	 */
	static Matrix4 identity() {
		return of(
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1
		);
	}

	/**
	 * Returns a new 4x4 matrix initialized to {@code other}.
	 */
	static Matrix4 of(Matrix4 other) {
		return of(
				other.getXx(), other.getXy(), other.getXz(), other.getXw(),
				other.getYx(), other.getYy(), other.getYz(), other.getYw(),
				other.getZx(), other.getZy(), other.getZz(), other.getZw(),
				other.getWx(), other.getWy(), other.getWz(), other.getWw()
		);
	}

	/**
	 * Returns a new 4x4 matrix for the given configuration.
	 */
	static Matrix4 of(
			double xx, double xy, double xz, double xw,
			double yx, double yy, double yz, double yw,
			double zx, double zy, double zz, double zw,
			double wx, double wy, double wz, double ww
	) {
		return new Value(
				xx, xy, xz, xw,
				yx, yy, yz, yw,
				zx, zy, zz, zw,
				wx, wy, wz, ww
		);
	}

	/**
	 * Generic {@link Object#equals(Object)} implementation for {@code Matrix4} instances.
	 * Uses {@code instanceof}, rather than a class equality check.
	 */
	static boolean equals(Matrix4 matrix, Object obj) {
		if (matrix == obj) return true;
		if (!(obj instanceof Matrix4 o)) return false;
		return FloatOps.equals(matrix.getXx(), o.getXx()) && FloatOps.equals(matrix.getXy(), o.getXy()) && FloatOps.equals(matrix.getXz(), o.getXz()) && FloatOps.equals(matrix.getXw(), o.getXw())
				&& FloatOps.equals(matrix.getYx(), o.getYx()) && FloatOps.equals(matrix.getYy(), o.getYy()) && FloatOps.equals(matrix.getYz(), o.getYz()) && FloatOps.equals(matrix.getYw(), o.getYw())
				&& FloatOps.equals(matrix.getZx(), o.getZx()) && FloatOps.equals(matrix.getZy(), o.getZy()) && FloatOps.equals(matrix.getZz(), o.getZz()) && FloatOps.equals(matrix.getZw(), o.getZw())
				&& FloatOps.equals(matrix.getWx(), o.getWx()) && FloatOps.equals(matrix.getWy(), o.getWy()) && FloatOps.equals(matrix.getWz(), o.getWz()) && FloatOps.equals(matrix.getWw(), o.getWw());
	}
	/**
	 * Generic {@link Object#hashCode()} implementation for {@code Matrix4} instances.
	 */
	static int hashCode(Matrix4 matrix) {
		return Objects.hash(
				matrix.getXx(), matrix.getXy(), matrix.getXz(), matrix.getXw(),
				matrix.getYx(), matrix.getYy(), matrix.getYz(), matrix.getYw(),
				matrix.getZx(), matrix.getZy(), matrix.getZz(), matrix.getZw(),
				matrix.getWx(), matrix.getWy(), matrix.getWz(), matrix.getWw()
		);
	}
	/**
	 * Generic {@link Object#toString()} implementation for {@code Matrix4} instances.
	 */
	static String toString(Matrix4 matrix) {
		return String.format(
				"((%.9f,%.9f,%.9f,%.9f),(%.9f,%.9f,%.9f,%.9f),(%.9f,%.9f,%.9f,%.9f)),(%.9f,%.9f,%.9f,%.9f))",
				matrix.getXx(), matrix.getXy(), matrix.getXz(), matrix.getXw(),
				matrix.getYx(), matrix.getYy(), matrix.getYz(), matrix.getYw(),
				matrix.getZx(), matrix.getZy(), matrix.getZz(), matrix.getZw(),
				matrix.getWx(), matrix.getWy(), matrix.getWz(), matrix.getWw()
		);
	}

	@Override
	default void scale(double value) {
		setXx(getXx() * value);
		setXy(getXy() * value);
		setYx(getYx() * value);
		setYy(getYy() * value);
		setXz(getXz() * value);
		setYz(getYz() * value);
		setZx(getZx() * value);
		setZy(getZy() * value);
		setZz(getZz() * value);
		setXw(getXw() * value);
		setYw(getYw() * value);
		setZw(getZw() * value);
		setWx(getWx() * value);
		setWy(getWy() * value);
		setWz(getWz() * value);
		setWw(getWw() * value);
	}

	/**
	 * Sets this matrix to match {@code other}.
	 */
	default void set(Matrix4 other) {
		setXx(other.getXx());
		setXy(other.getXy());
		setXz(other.getXz());
		setXw(other.getXw());
		setYx(other.getYx());
		setYy(other.getYy());
		setYz(other.getYz());
		setYw(other.getYw());
		setZx(other.getZx());
		setZy(other.getZy());
		setZz(other.getZz());
		setZw(other.getZw());
		setWx(other.getWx());
		setWy(other.getWy());
		setWz(other.getWz());
		setWw(other.getWw());
	}

	/**
	 * Adds {@code other}'s components to this matrix.
	 */
	default void add(Matrix4 other) {
		add((Matrix3) other);
		setXw(getXw() + other.getXw());
		setYw(getYw() + other.getYw());
		setZw(getZw() + other.getZw());
		setWx(getWx() + other.getWx());
		setWy(getWy() + other.getWy());
		setWz(getWz() + other.getWz());
		setWw(getWw() + other.getWw());
	}
	/**
	 * Adds {@code scale} proportion of {@code other}'s components to this matrix.
	 */
	default void add(Matrix4 other, double scale) {
		add((Matrix3) other, scale);
		setXw(getXw() + other.getXw() * scale);
		setYw(getYw() + other.getYw() * scale);
		setZw(getZw() + other.getZw() * scale);
		setWx(getWx() + other.getWx() * scale);
		setWy(getWy() + other.getWy() * scale);
		setWz(getWz() + other.getWz() * scale);
		setWw(getWw() + other.getWw() * scale);
	}

	/**
	 * Matrix-multiplies this matrix with {@code other} and sets the result on this matrix.
	 */
	default void multiply(Matrix4 other) {
		double newXx = getXx() * other.getXx() + getXy() * other.getYx() + getXz() * other.getZx() + getXw() * other.getWx();
		double newXy = getXx() * other.getXy() + getXy() * other.getYy() + getXz() * other.getZy() + getXw() * other.getWy();
		double newXz = getXx() * other.getXz() + getXy() * other.getYz() + getXz() * other.getZz() + getXw() * other.getWz();
		double newXw = getXx() * other.getXw() + getXy() * other.getYw() + getXz() * other.getZw() + getXw() * other.getWw();

		double newYx = getYx() * other.getXx() + getYy() * other.getYx() + getYz() * other.getZx() + getYw() * other.getWx();
		double newYy = getYx() * other.getXy() + getYy() * other.getYy() + getYz() * other.getZy() + getYw() * other.getWy();
		double newYz = getYx() * other.getXz() + getYy() * other.getYz() + getYz() * other.getZz() + getYw() * other.getWz();
		double newYw = getYx() * other.getXw() + getYy() * other.getYw() + getYz() * other.getZw() + getYw() * other.getWw();

		double newZx = getZx() * other.getXx() + getZy() * other.getYx() + getZz() * other.getZx() + getZw() * other.getWx();
		double newZy = getZx() * other.getXy() + getZy() * other.getYy() + getZz() * other.getZy() + getZw() * other.getWy();
		double newZz = getZx() * other.getXz() + getZy() * other.getYz() + getZz() * other.getZz() + getZw() * other.getWz();
		double newZw = getZx() * other.getXw() + getZy() * other.getYw() + getZz() * other.getZw() + getZw() * other.getWw();

		double newWx = getWx() * other.getXx() + getWy() * other.getYx() + getWz() * other.getZx() + getWw() * other.getWx();
		double newWy = getWx() * other.getXy() + getWy() * other.getYy() + getWz() * other.getZy() + getWw() * other.getWy();
		double newWz = getWx() * other.getXz() + getWy() * other.getYz() + getWz() * other.getZz() + getWw() * other.getWz();
		double newWw = getWx() * other.getXw() + getWy() * other.getYw() + getWz() * other.getZw() + getWw() * other.getWw();

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
	default void translate(Vector3 translation) {
		Matrix4 op = Matrix4.identity();

		op.setXw(getXw() + translation.getX());
		op.setYw(getYw() + translation.getY());
		op.setZw(getZw() + translation.getZ());

		multiply(op);
	}
	/**
	 * Applies a rotation of {@code radians} about {@code axis} on this matrix.
	 * Intended for building a transformation matrix from an {@link #identity()} matrix.
	 * Should be applied after translation but before scaling.
	 * Assumes that {@code axis} is a unit vector.
	 * @see #scale(Vector3)
	 * @see #translate(Vector3)
	 */
	// FIXME this has to be called in reverse order for multiple rotations, right?
	default void rotate(double radians, Vector3 axis) {
		double cosTheta = Math.cos(radians);
		double sinTheta = Math.sin(radians);
		double iCosTheta = 1 - cosTheta;

		Matrix4 op = Matrix4.identity();

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
	default void scale(Vector3 scale) {
		Matrix4 op = Matrix4.identity();

		op.setXx(scale.getX());
		op.setYy(scale.getY());
		op.setZz(scale.getZ());

		multiply(op);
	}

	/**
	 * Sets this matrix to the {@link #identity()} matrix.
	 */
	default void reset() {
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

	double getXw();
	void setXw(double xw);

	double getYw();
	void setYw(double yw);

	double getZw();
	void setZw(double zw);

	double getWx();
	void setWx(double wx);

	double getWy();
	void setWy(double wy);

	double getWz();
	void setWz(double wz);

	double getWw();
	void setWw(double ww);

	final class Value extends Matrix3.Value implements Matrix4 {
		private double xw, yw, zw, wx, wy, wz, ww;

		Value(
				double xx, double xy, double xz, double xw,
				double yx, double yy, double yz, double yw,
				double zx, double zy, double zz, double zw,
				double wx, double wy, double wz, double ww
		) {
			super(
					xx, xy, xz,
					yx, yy, yz,
					zx, zy, zz
			);
			setXw(xw);
			setYw(yw);
			setZw(zw);
			setWx(wx);
			setWy(wy);
			setWz(wz);
			setWw(ww);
		}

		@Override
		public double getXw() {
			return xw;
		}
		@Override
		public void setXw(double xw) {
			this.xw = FloatOps.sanitize(xw);
		}

		@Override
		public double getYw() {
			return yw;
		}
		@Override
		public void setYw(double yw) {
			this.yw = FloatOps.sanitize(yw);
		}

		@Override
		public double getZw() {
			return zw;
		}
		@Override
		public void setZw(double zw) {
			this.zw = FloatOps.sanitize(zw);
		}

		@Override
		public double getWx() {
			return wx;
		}
		@Override
		public void setWx(double wx) {
			this.wx = FloatOps.sanitize(wx);
		}

		@Override
		public double getWy() {
			return wy;
		}
		@Override
		public void setWy(double wy) {
			this.wy = FloatOps.sanitize(wy);
		}

		@Override
		public double getWz() {
			return wz;
		}
		@Override
		public void setWz(double wz) {
			this.wz = FloatOps.sanitize(wz);
		}

		@Override
		public double getWw() {
			return ww;
		}
		@Override
		public void setWw(double ww) {
			this.ww = FloatOps.sanitize(ww);
		}

		@Override
		public boolean equals(Object obj) {
			return Matrix4.equals(this, obj);
		}
		@Override
		public int hashCode() {
			return Matrix4.hashCode(this);
		}
		@Override
		public String toString() {
			return Matrix4.toString(this);
		}
	}
}
