package dev.kkorolyov.pancake.platform.media.graphic;

import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * Transform settings for rendering a {@link Renderable}.
 */
public class RenderTransform {
	private final Vector position = new Vector();
	private final Vector rotation = new Vector();
	private final Vector scale = new Vector();

	/**
	 * Constructs a new render transform.
	 */
	public RenderTransform() {
		reset();
	}

	/** @return render position in {@code px} along each axis */
	public Vector getPosition() {
		return position;
	}
	public RenderTransform setPosition(Vector position) {
		this.position.set(position);
		return this;
	}

	/** @return rotation in {@code rad} along each axis */
	public Vector getRotation() {
		return rotation;
	}
	public RenderTransform setRotation(Vector rotation) {
		this.rotation.set(rotation);
		return this;
	}

	/** @return scaling by proportion along each axis */
	public Vector getScale() {
		return scale;
	}
	public RenderTransform setScale(Vector scale) {
		this.scale.set(scale);
		return this;
	}

	/**
	 * Resets all components to default, non-modifying values.
	 * @return {@code this}
	 */
	public RenderTransform reset() {
		position.set(0, 0, 0);
		rotation.set(0, 0, 0);
		scale.set(1, 1, 1);

		return this;
	}
}
