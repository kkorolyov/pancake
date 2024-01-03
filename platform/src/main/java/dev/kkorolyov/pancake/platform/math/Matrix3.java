package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A 3x3 matrix.
 */
public interface Matrix3 extends Matrix2 {
	/**
	 * Returns the determinant of {@code matrix}.
	 */
	static double determinant(Matrix3 matrix) {
		return matrix.getXx() * matrix.getYy() * matrix.getZz()
				- matrix.getXx() * matrix.getYz() * matrix.getZy()
				- matrix.getXy() * matrix.getYx() * matrix.getZz()
				+ matrix.getXy() * matrix.getYz() * matrix.getZx()
				+ matrix.getXz() * matrix.getYx() * matrix.getZy()
				- matrix.getXz() * matrix.getYy() * matrix.getZx();
	}

	/**
	 * Returns a new 3x3 identity matrix.
	 */
	static Matrix3 of() {
		return of(
				1, 0, 0,
				0, 1, 0,
				0, 0, 1
		);
	}

	/**
	 * Returns a new 3x3 matrix for the given configuration.
	 */
	static Matrix3 of(
			double xx, double xy, double xz,
			double yx, double yy, double yz,
			double zx, double zy, double zz
	) {
		return new Value(
				xx, xy, xz,
				yx, yy, yz,
				zx, zy, zz
		);
	}

	/**
	 * Generic {@link Object#equals(Object)} implementation for {@code Matrix3} instances.
	 * Uses {@code instanceof}, rather than a class equality check.
	 */
	static boolean equals(Matrix3 matrix, Object obj) {
		if (matrix == obj) return true;
		if (!(obj instanceof Matrix3 o)) return false;
		return FloatOps.equals(matrix.getXx(), o.getXx()) && FloatOps.equals(matrix.getXy(), o.getXy()) && FloatOps.equals(matrix.getXz(), o.getXz())
				&& FloatOps.equals(matrix.getYx(), o.getYx()) && FloatOps.equals(matrix.getYy(), o.getYy()) && FloatOps.equals(matrix.getYz(), o.getYz())
				&& FloatOps.equals(matrix.getZx(), o.getZx()) && FloatOps.equals(matrix.getZy(), o.getZy()) && FloatOps.equals(matrix.getZz(), o.getZz());
	}
	/**
	 * Generic {@link Object#hashCode()} implementation for {@code Matrix3} instances.
	 */
	static int hashCode(Matrix3 matrix) {
		return Objects.hash(
				matrix.getXx(), matrix.getXy(), matrix.getXz(),
				matrix.getYx(), matrix.getYy(), matrix.getYz(),
				matrix.getZx(), matrix.getZy(), matrix.getZz()
		);
	}
	/**
	 * Generic {@link Object#toString()} implementation for {@code Matrix3} instances.
	 */
	static String toString(Matrix3 matrix) {
		return String.format(
				"((%.9f,%.9f,%.9f),(%.9f,%.9f,%.9f),(%.9f,%.9f,%.9f))",
				matrix.getXx(), matrix.getXy(), matrix.getXz(),
				matrix.getYx(), matrix.getYy(), matrix.getYz(),
				matrix.getZx(), matrix.getZy(), matrix.getZz()
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
	}

	/**
	 * Adds {@code other}'s components to this matrix.
	 */
	default void add(Matrix3 other) {
		add((Matrix2) other);
		setXz(getXz() + other.getXz());
		setYz(getYz() + other.getYz());
		setZx(getZx() + other.getZx());
		setZy(getZy() + other.getZy());
		setZz(getZz() + other.getZz());
	}
	/**
	 * Adds {@code scale} proportion of {@code other}'s components to this matrix.
	 */
	default void add(Matrix3 other, double scale) {
		add((Matrix2) other, scale);
		setXz(getXz() + other.getXz() * scale);
		setYz(getYz() + other.getYz() * scale);
		setZx(getZx() + other.getZx() * scale);
		setZy(getZy() + other.getZy() * scale);
		setZz(getZz() + other.getZz() * scale);
	}

	double getXz();
	void setXz(double xz);

	double getYz();
	void setYz(double yz);

	double getZx();
	void setZx(double zx);

	double getZy();
	void setZy(double zy);

	double getZz();
	void setZz(double zz);

	sealed class Value extends Matrix2.Value implements Matrix3 permits Matrix4.Value {
		private double xz, yz, zx, zy, zz;

		Value(
				double xx, double xy, double xz,
				double yx, double yy, double yz,
				double zx, double zy, double zz
		) {
			super(
					xx, xy,
					yx, yy
			);
			setXz(xz);
			setYz(yz);
			setZx(zx);
			setZy(zy);
			setZz(zz);
		}

		@Override
		public final double getXz() {
			return xz;
		}
		@Override
		public final void setXz(double xz) {
			this.xz = FloatOps.sanitize(xz);
		}

		@Override
		public final double getYz() {
			return yz;
		}
		@Override
		public final void setYz(double yz) {
			this.yz = FloatOps.sanitize(yz);
		}

		@Override
		public final double getZx() {
			return zx;
		}
		@Override
		public final void setZx(double zx) {
			this.zx = FloatOps.sanitize(zx);
		}

		@Override
		public final double getZy() {
			return zy;
		}
		@Override
		public final void setZy(double zy) {
			this.zy = FloatOps.sanitize(zy);
		}

		@Override
		public final double getZz() {
			return zz;
		}
		@Override
		public final void setZz(double zz) {
			this.zz = FloatOps.sanitize(zz);
		}

		@Override
		public boolean equals(Object obj) {
			return Matrix3.equals(this, obj);
		}
		@Override
		public int hashCode() {
			return Matrix3.hashCode(this);
		}
		@Override
		public String toString() {
			return Matrix3.toString(this);
		}
	}
}
