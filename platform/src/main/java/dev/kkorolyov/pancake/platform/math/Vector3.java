package dev.kkorolyov.pancake.platform.math;

import java.util.Objects;

/**
 * A head at some point in 3 dimensions and tail at {@code (0, 0, 0)}.
 */
public interface Vector3 extends Vector2 {
	/**
	 * Returns the length of {@code vector}.
	 */
	static double magnitude(Vector3 vector) {
		return FloatOps.sanitize(Math.sqrt(dot(vector, vector)));
	}
	/**
	 * Returns the dot product of {@code a} and {@code b}.
	 */
	static double dot(Vector3 a, Vector3 b) {
		return FloatOps.sanitize(a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ());
	}
	/**
	 * Returns the Euclidean distance between {@code a} and {@code b}.
	 */
	static double distance(Vector3 a, Vector3 b) {
		return FloatOps.sanitize(
				Math.sqrt(
						Math.pow(a.getX() - b.getX(), 2) +
								Math.pow(a.getY() - b.getY(), 2) +
								Math.pow(a.getZ() - b.getZ(), 2)
				)
		);
	}
	/**
	 * Returns the angle in radians between {@code c} and {@code b}.
	 */
	static double angle(Vector3 a, Vector3 b) {
		double mag = magnitude(a) * magnitude(b);
		return FloatOps.equals(0, mag) ? 0 : FloatOps.sanitize(
				Math.acos(
						dot(a, b) / mag
				)
		);
	}

	/**
	 * Returns a vector decorating {@code delegate} and invoking {@code onChange} any time it is modified.
	 */
	static Vector3 observable(Vector3 delegate, Runnable onChange) {
		return new Observable(delegate, onChange);
	}
	/**
	 * Returns a read-only view of {@code delegate}.
	 */
	static Vector3 readOnly(Vector3 delegate) {
		return new ReadOnly(delegate);
	}

	/**
	 * Returns a 3-dimensional vector initialized to {@code other}.
	 */
	static Vector3 of(Vector2 other) {
		return of(other.getX(), other.getY());
	}
	/**
	 * Returns a 3-dimensional vector initialized to {@code other}.
	 */
	static Vector3 of(Vector3 other) {
		return of(other.getX(), other.getY(), other.getZ());
	}

	/**
	 * Returns a 3-dimensional vector initialized to {@code (0, 0, 0)}.
	 */
	static Vector3 of() {
		return of(0);
	}
	/**
	 * Returns a 3-dimensional vector initialized to {@code (x, 0, 0)}.
	 */
	static Vector3 of(double x) {
		return of(x, 0);
	}
	/**
	 * Returns a 3-dimensional vector initialized to {@code (x, y, 0)}.
	 */
	static Vector3 of(double x, double y) {
		return of(x, y, 0);
	}
	/**
	 * Returns a 3-dimensional vector initialized to {@code (x, y, z)}.
	 */
	static Vector3 of(double x, double y, double z) {
		return new Value(x, y, z);
	}

	/**
	 * Generic {@link Object#equals(Object)} implementation for {@code Vector3} instances.
	 * Uses {@code instanceof}, rather than a class equality check.
	 */
	static boolean equals(Vector3 vector, Object obj) {
		if (vector == obj) return true;
		if (!(obj instanceof Vector3 o)) return false;
		return FloatOps.equals(vector.getX(), o.getX()) && FloatOps.equals(vector.getY(), o.getY()) && FloatOps.equals(vector.getZ(), o.getZ());
	}
	/**
	 * Generic {@link Object#hashCode()} implementation for {@code Vector3} instances.
	 */
	static int hashCode(Vector3 vector) {
		return Objects.hash(vector.getX(), vector.getY(), vector.getZ());
	}
	/**
	 * Generic {@link Object#toString()} implementation for {@code Vector3} instances.
	 */
	static String toString(Vector3 vector) {
		return String.format("(%.9f,%.9f,%.9f)", vector.getX(), vector.getY(), vector.getZ());
	}

	/**
	 * Resizes this vector to length {@code 1}.
	 */
	@Override
	default void normalize() {
		double magnitude = magnitude(this);
		scale(magnitude == 0 ? 0 : 1 / magnitude);
	}
	/**
	 * Projects this vector along {@code other}.
	 */
	default void project(Vector3 other) {
		double scale = dot(this, other) / dot(other, other);
		set(other);
		scale(scale);
	}
	/**
	 * Reflects this vector along {@code other}.
	 */
	default void reflect(Vector3 other) {
		add(other, -2 * dot(this, other) / dot(other, other));
	}

	@Override
	default void scale(double value) {
		setX(getX() * value);
		setY(getY() * value);
		setZ(getZ() * value);
	}

	/**
	 * Sets this vector equal to {@code other}.
	 * @param other vector to match
	 */
	default void set(Vector3 other) {
		set((Vector2) other);
		setZ(other.getZ());
	}

	/**
	 * Translates the head of this vector by {@code other}.
	 */
	default void add(Vector3 other) {
		add((Vector2) other);
		setZ(getZ() + other.getZ());
	}
	/**
	 * Translates the head of this vector by {@code scale} proportion of {@code other}.
	 */
	default void add(Vector3 other, double scale) {
		add((Vector2) other, scale);
		setZ(getZ() + other.getZ() * scale);
	}

	/**
	 * Transforms the head of this vector according to {@code transform}.
	 */
	default void transform(Matrix4 transform) {
		var newX = getX() * transform.getXx() + getY() * transform.getXy() + getZ() * transform.getXz() + transform.getXw();
		var newY = getX() * transform.getYx() + getY() * transform.getYy() + getZ() * transform.getYz() + transform.getYw();
		var newZ = getX() * transform.getZx() + getY() * transform.getZy() + getZ() * transform.getZz() + transform.getZw();

		setX(newX);
		setY(newY);
		setZ(newZ);
	}

	@Override
	default void reset() {
		setX(0);
		setY(0);
		setZ(0);
	}
	double getZ();
	void setZ(double z);

	/**
	 * Basic mutable value-based {@code Vector3} implementation.
	 */
	final class Value extends Vector2.Value implements Vector3 {
		private double z;

		Value(double x, double y, double z) {
			super(x, y);
			setZ(z);
		}

		public double getZ() {
			return z;
		}
		public void setZ(double z) {
			this.z = FloatOps.sanitize(z);
		}

		@Override
		public boolean equals(Object obj) {
			return Vector3.equals(this, obj);
		}
		@Override
		public int hashCode() {
			return Vector3.hashCode(this);
		}

		@Override
		public String toString() {
			return Vector3.toString(this);
		}
	}

	/**
	 * Forwards all calls to a delegate vector, and invokes a callback when this vector is modified.
	 */
	final class Observable implements Vector3 {
		private final Vector3 delegate;
		private final Runnable onChange;

		Observable(Vector3 delegate, Runnable onChange) {
			this.delegate = delegate;
			this.onChange = onChange;
		}

		@Override
		public double getX() {
			return delegate.getX();
		}
		@Override
		public void setX(double x) {
			delegate.setX(x);
			onChange.run();
		}

		@Override
		public double getY() {
			return delegate.getY();
		}
		@Override
		public void setY(double y) {
			delegate.setY(y);
			onChange.run();
		}

		@Override
		public double getZ() {
			return delegate.getZ();
		}
		@Override
		public void setZ(double z) {
			delegate.setZ(z);
			onChange.run();
		}

		@Override
		public boolean equals(Object obj) {
			return Vector3.equals(this, obj);
		}
		@Override
		public int hashCode() {
			return Vector3.hashCode(this);
		}

		@Override
		public String toString() {
			return Vector3.toString(this);
		}
	}

	/**
	 * A read-only view of a vector.
	 */
	final class ReadOnly implements Vector3 {
		private final Vector3 delegate;

		ReadOnly(Vector3 delegate) {
			this.delegate = delegate;
		}

		private static void throwE() {
			throw new UnsupportedOperationException("cannot modify this vector");
		}

		@Override
		public double getX() {
			return delegate.getX();
		}
		@Override
		public void setX(double x) {
			throwE();
		}

		@Override
		public double getY() {
			return delegate.getY();
		}
		@Override
		public void setY(double y) {
			throwE();
		}

		@Override
		public double getZ() {
			return delegate.getZ();
		}
		@Override
		public void setZ(double z) {
			throwE();
		}
	}
}
