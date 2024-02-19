package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Matrix4;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.ArrayList;

/**
 * Translation, rotation, and scale of an entity in space.
 * All operations are relative to the transform's parent.
 */
public class Transform implements Component {
	private final Vector3 translation = Vector3.observable(Vector3.of(), this::invalidate);
	private final Matrix4 rotation = new BoundMatrix4(Matrix4.of());
	private final Vector3 scale = Vector3.observable(Vector3.of(1, 1, 1), this::invalidate);

	private Transform parent;
	private final ArrayList<Transform> children = new ArrayList<>();

	private final Matrix4 matrix = Matrix4.of();
	private final Matrix4 resultMatrix = new ReadOnlyMatrix4(matrix);
	private boolean cached;

	/**
	 * Returns the mutable translation vector.
	 */
	public Vector3 getTranslation() {
		return translation;
	}
	/**
	 * Returns the mutable rotation matrix.
	 */
	public Matrix4 getRotation() {
		return rotation;
	}
	/**
	 * Returns the mutable scale vector.
	 */
	public Vector3 getScale() {
		return scale;
	}

	/**
	 * Returns the parent transform, if any.
	 * No parent implies that this transform resides at the top-level - or root - coordinate space.
	 */
	public Transform getParent() {
		return parent;
	}
	/**
	 * Sets direct transform {@code parent}.
	 */
	public void setParent(Transform parent) {
		if (this.parent != null) this.parent.children.remove(this);
		this.parent = parent;
		this.parent.children.add(this);
	}

	/**
	 * Returns the matrix for transforming coordinates from this transform's local space to root space.
	 * The returned instance cannot be modified.
	 */
	public Matrix4 getMatrix() {
		if (!cached) {
			matrix.reset();
			matrix.translate(translation);
			matrix.multiply(rotation);
			matrix.scale(scale);

			if (parent != null) matrix.multiplyTo(parent.getMatrix());

			cached = true;
		}
		return resultMatrix;
	}

	private void invalidate() {
		cached = false;
		for (int i = 0; i < children.size(); i++) children.get(i).invalidate();
	}

	private final class BoundMatrix4 implements Matrix4 {
		private final Matrix4 data;

		BoundMatrix4(Matrix4 data) {
			this.data = data;
		}

		@Override
		public double getXx() {
			return data.getXx();
		}
		@Override
		public void setXx(double xx) {
			data.setXx(xx);
			invalidate();
		}

		@Override
		public double getXy() {
			return data.getXy();
		}
		@Override
		public void setXy(double xy) {
			data.setXy(xy);
			invalidate();
		}

		@Override
		public double getYx() {
			return data.getYx();
		}
		@Override
		public void setYx(double yx) {
			data.setYx(yx);
			invalidate();
		}

		@Override
		public double getYy() {
			return data.getYy();
		}
		@Override
		public void setYy(double yy) {
			data.setYy(yy);
			invalidate();
		}

		@Override
		public double getXz() {
			return data.getXz();
		}
		@Override
		public void setXz(double xz) {
			data.setXz(xz);
			invalidate();
		}

		@Override
		public double getYz() {
			return data.getYz();
		}
		@Override
		public void setYz(double yz) {
			data.setYz(yz);
			invalidate();
		}

		@Override
		public double getZx() {
			return data.getZx();
		}
		@Override
		public void setZx(double zx) {
			data.setZx(zx);
			invalidate();
		}

		@Override
		public double getZy() {
			return data.getZy();
		}
		@Override
		public void setZy(double zy) {
			data.setZy(zy);
			invalidate();
		}

		@Override
		public double getZz() {
			return data.getZz();
		}
		@Override
		public void setZz(double zz) {
			data.setZz(zz);
			invalidate();
		}

		@Override
		public double getXw() {
			return data.getXw();
		}
		@Override
		public void setXw(double xw) {
			data.setXw(xw);
			invalidate();
		}

		@Override
		public double getYw() {
			return data.getYw();
		}
		@Override
		public void setYw(double yw) {
			data.setYw(yw);
			invalidate();
		}

		@Override
		public double getZw() {
			return data.getZw();
		}
		@Override
		public void setZw(double zw) {
			data.setZw(zw);
			invalidate();
		}

		@Override
		public double getWx() {
			return data.getWx();
		}
		@Override
		public void setWx(double wx) {
			data.setWx(wx);
			invalidate();
		}

		@Override
		public double getWy() {
			return data.getWy();
		}
		@Override
		public void setWy(double wy) {
			data.setWy(wy);
			invalidate();
		}

		@Override
		public double getWz() {
			return data.getWz();
		}
		@Override
		public void setWz(double wz) {
			data.setWz(wz);
			invalidate();
		}

		@Override
		public double getWw() {
			return data.getWw();
		}
		@Override
		public void setWw(double ww) {
			data.setWw(ww);
			invalidate();
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

	private static final class ReadOnlyMatrix4 implements Matrix4 {
		private final Matrix4 data;

		private ReadOnlyMatrix4(Matrix4 data) {
			this.data = data;
		}

		@Override
		public double getXx() {
			return data.getXx();
		}
		@Override
		public void setXx(double xx) {}

		@Override
		public double getXy() {
			return data.getXy();
		}
		@Override
		public void setXy(double xy) {}

		@Override
		public double getYx() {
			return data.getYx();
		}
		@Override
		public void setYx(double yx) {}

		@Override
		public double getYy() {
			return data.getYy();
		}
		@Override
		public void setYy(double yy) {}

		@Override
		public double getXz() {
			return data.getXz();
		}
		@Override
		public void setXz(double xz) {}

		@Override
		public double getYz() {
			return data.getYz();
		}
		@Override
		public void setYz(double yz) {
		}

		@Override
		public double getZx() {
			return data.getZx();
		}
		@Override
		public void setZx(double zx) {}

		@Override
		public double getZy() {
			return data.getZy();
		}
		@Override
		public void setZy(double zy) {}

		@Override
		public double getZz() {
			return data.getZz();
		}
		@Override
		public void setZz(double zz) {}

		@Override
		public double getXw() {
			return data.getXw();
		}
		@Override
		public void setXw(double xw) {}

		@Override
		public double getYw() {
			return data.getYw();
		}
		@Override
		public void setYw(double yw) {}

		@Override
		public double getZw() {
			return data.getZw();
		}
		@Override
		public void setZw(double zw) {}

		@Override
		public double getWx() {
			return data.getWx();
		}
		@Override
		public void setWx(double wx) {}

		@Override
		public double getWy() {
			return data.getWy();
		}
		@Override
		public void setWy(double wy) {}

		@Override
		public double getWz() {
			return data.getWz();
		}
		@Override
		public void setWz(double wz) {}

		@Override
		public double getWw() {
			return data.getWw();
		}
		@Override
		public void setWw(double ww) {}

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
