package dev.kkorolyov.pancake.platform.math;

/**
 * Provides vector implementations.
 * @see Vector2
 * @see Vector3
 */
public final class Vectors {
	private Vectors() {}

	/** @return 1-dimensional vector initialized to {@code other} */
	public static Vector1 create(Vector1 other) {
		return create(other.getX());
	}
	/** @return 2-dimensional vector initialized to {@code other} */
	public static Vector2 create(Vector2 other) {
		return create(other.getX(), other.getY());
	}
	/** @return 3-dimensional vector initialized to {@code other} */
	public static Vector3 create(Vector3 other) {
		return create(other.getX(), other.getY(), other.getZ());
	}

	/** @return 1-dimensional vector initialized to {@code (x)} */
	public static Vector1 create(double x) {
		return new BasicVector1(x);
	}
	/** @return 2-dimensional vector initialized to {@code (x, y)} */
	public static Vector2 create(double x, double y) {
		return new BasicVector2(x, y);
	}
	/** @return 3-dimensional vector initialized to {@code (x, y, z)} */
	public static Vector3 create(double x, double y, double z) {
		return new BasicVector3(x, y, z);
	}

	private static class BasicVector1 implements Vector1 {
		private double x;

		BasicVector1(double x) {
			this.x = x;
		}

		@Override
		public double getX() {
			return x;
		}
		@Override
		public void setX(double value) {
			x = value;
		}

		@Override
		public void scale(double value) {
			x *= value;
		}

		@Override
		public void set(Vector1 other) {
			x = other.getX();
		}
		@Override
		public void add(Vector1 other) {
			x += other.getX();
		}
		@Override
		public void add(Vector1 other, double scale) {
			x += other.getX() * scale;
		}
	}

	private static class BasicVector2 extends BasicVector1 implements Vector2 {
		private double y;

		BasicVector2(double x, double y) {
			super(x);
			this.y = y;
		}

		@Override
		public double getY() {
			return y;
		}
		@Override
		public void setY(double value) {
			y = value;
		}

		@Override
		public void scale(double value) {
			super.scale(value);
			y *= value;
		}

		@Override
		public void set(Vector2 other) {
			set((Vector1) other);
			y = other.getY();
		}
		@Override
		public void add(Vector2 other) {
			add((Vector1) other);
			y += other.getY();
		}
		@Override
		public void add(Vector2 other, double scale) {
			add((Vector1) other, scale);
			y += other.getY() * scale;
		}
	}

	private static class BasicVector3 extends BasicVector2 implements Vector3 {
		private double z;

		BasicVector3(double x, double y, double z) {
			super(x, y);
			this.z = z;
		}

		@Override
		public double getZ() {
			return z;
		}
		@Override
		public void setZ(double value) {
			z = value;
		}

		@Override
		public void scale(double value) {
			super.scale(value);
			z *= value;
		}

		@Override
		public void set(Vector3 other) {
			set((Vector2) other);
			z = other.getZ();
		}
		@Override
		public void add(Vector3 other) {
			add((Vector2) other);
			z += other.getZ();
		}
		@Override
		public void add(Vector3 other, double scale) {
			add((Vector2) other, scale);
			z += other.getZ() * scale;
		}
	}
}
