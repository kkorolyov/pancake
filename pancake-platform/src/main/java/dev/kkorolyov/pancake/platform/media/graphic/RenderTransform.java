package dev.kkorolyov.pancake.platform.media.graphic;

import dev.kkorolyov.pancake.platform.math.Vector1;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vectors;

/**
 * Transform settings for rendering a {@link Renderable}.
 */
public final class RenderTransform {
	private final Vector2 position = Vectors.create(0, 0);
	private final Vector1 rotation = Vectors.create(0);
	private final Vector2 scale = Vectors.create(1, 1);

	/**
	 * Constructs a new render transform.
	 */
	public RenderTransform() {
		reset();
	}

	/** @return render position in {@code px} along each axis */
	public Vector2 getPosition() {
		return position;
	}
	public RenderTransform setPosition(Vector2 position) {
		this.position.set(position);
		return this;
	}

	/** @return rotation in {@code rad} along each axis */
	public Vector1 getRotation() {
		return rotation;
	}
	public RenderTransform setRotation(Vector1 rotation) {
		this.rotation.set(rotation);
		return this;
	}

	/** @return scaling by proportion along each axis */
	public Vector2 getScale() {
		return scale;
	}
	public RenderTransform setScale(Vector2 scale) {
		this.scale.set(scale);
		return this;
	}

	/**
	 * Resets all components to default, non-modifying values.
	 * @return {@code this}
	 */
	public RenderTransform reset() {
		position.setX(0);
		position.setY(0);

		rotation.setX(0);

		scale.setX(1);
		scale.setY(1);

		return this;
	}
}
