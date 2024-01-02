package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A 2x2 matrix.
 */
public interface Matrix2 {
	/**
	 * Returns the determinant of {@code matrix}.
	 */
	static double determinant(Matrix2 matrix) {
		return matrix.getXx() * matrix.getYy() - matrix.getXy() * matrix.getYx();
	}

	/**
	 * Returns a new 2x2 identity matrix.
	 */
	static Matrix2 identity() {
		return of(
				1, 0,
				0, 1
		);
	}
	/**
	 * Returns a new 2x2 matrix for the given configuration.
	 */
	static Matrix2 of(
			double xx, double xy,
			double yx, double yy
	) {
		return new Value(
				xx, xy,
				yx, yy
		);
	}

	/**
	 * Generic {@link Object#equals(Object)} implementation for {@code Matrix2} instances.
	 * Uses {@code instanceof}, rather than a class equality check.
	 */
	static boolean equals(Matrix2 matrix, Object obj) {
		if (matrix == obj) return true;
		if (!(obj instanceof Matrix2 o)) return false;
		return FloatOps.equals(matrix.getXx(), o.getXx()) && FloatOps.equals(matrix.getXy(), o.getXy())
				&& FloatOps.equals(matrix.getYx(), o.getYx()) && FloatOps.equals(matrix.getYy(), o.getYy());
	}
	/**
	 * Generic {@link Object#hashCode()} implementation for {@code Matrix2} instances.
	 */
	static int hashCode(Matrix2 matrix) {
		return Objects.hash(
				matrix.getXx(), matrix.getXy(),
				matrix.getYx(), matrix.getYy()
		);
	}
	/**
	 * Generic {@link Object#toString()} implementation for {@code Matrix2} instances.
	 */
	static String toString(Matrix2 matrix) {
		return String.format(
				"((%.9f,%.9f),(%.9f,%.9f))",
				matrix.getXx(), matrix.getXy(),
				matrix.getYx(), matrix.getYy()
		);
	}

	/**
	 * Sets this matrix to its inverse.
	 */
	default void invert() {
		var determinant = determinant(this);

		setXx(getYy());
		setXy(-getXy());
		setYx(-getYx());
		setYy(getXx());

		scale(1 / determinant);
	}

	/**
	 * Scales this matrix by {@code value}.
	 */
	default void scale(double value) {
		setXx(getXx() * value);
		setXy(getXy() * value);
		setYx(getYx() * value);
		setYy(getYy() * value);
	}

	/**
	 * Adds {@code other}'s components to this matrix.
	 */
	default void add(Matrix2 other) {
		setXx(getXx() + other.getXx());
		setXy(getXy() + other.getXy());
		setYx(getYx() + other.getYx());
		setYy(getYy() + other.getYy());
	}
	/**
	 * Adds {@code scale} proportion of {@code other}'s components to this matrix.
	 */
	default void add(Matrix2 other, double scale) {
		setXx(getXx() + other.getXx() * scale);
		setXy(getXy() + other.getXy() * scale);
		setYx(getYx() + other.getYx() * scale);
		setYy(getYy() + other.getYy() * scale);
	}

	double getXx();
	void setXx(double xx);

	double getXy();
	void setXy(double xy);

	double getYx();
	void setYx(double yx);

	double getYy();
	void setYy(double yy);

	sealed class Value implements Matrix2 permits Matrix3.Value {
		private double xx, xy, yx, yy;

		Value(
				double xx, double xy,
				double yx, double yy
		) {
			setXx(xx);
			setXy(xy);
			setYx(yx);
			setYy(yy);
		}

		@Override
		public final double getXx() {
			return xx;
		}
		@Override
		public final void setXx(double xx) {
			this.xx = FloatOps.sanitize(xx);
		}

		@Override
		public final double getXy() {
			return xy;
		}
		@Override
		public final void setXy(double xy) {
			this.xy = FloatOps.sanitize(xy);
		}

		@Override
		public final double getYx() {
			return yx;
		}
		@Override
		public final void setYx(double yx) {
			this.yx = FloatOps.sanitize(yx);
		}

		@Override
		public final double getYy() {
			return yy;
		}
		@Override
		public final void setYy(double yy) {
			this.yy = FloatOps.sanitize(yy);
		}

		@Override
		public boolean equals(Object obj) {
			return Matrix2.equals(this, obj);
		}
		@Override
		public int hashCode() {
			return Matrix2.hashCode(this);
		}
		@Override
		public String toString() {
			return Matrix2.toString(this);
		}
	}
}
