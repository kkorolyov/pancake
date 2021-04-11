package dev.kkorolyov.pancake.platform.media;

import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vectors;

/**
 * A viewport on a render medium providing for mapping between absolute and relative/rendered positions.
 */
public final class Camera {
	private Vector2 position;
	private final double unitPixels;
	private final Vector2 output = Vectors.create(0, 0);
	private final Vector2 halfSize = Vectors.create(0, 0);

	/**
	 * Constructs a new camera.
	 * @param position absolute camera position
	 * @param unitPixels number of pixels within 1 unit of absolute distance
	 * @param width render medium width
	 * @param height render medium height
	 */
	public Camera(Vector2 position, double unitPixels, double width, double height) {
		this.position = Vectors.create(position);
		this.unitPixels = unitPixels;
		setSize(width, height);
	}

	/**
	 * Calculates and returns the absolute position of a relative position vector.
	 * The returned vector is shared by all invocations of this method and {@link #getRelativePosition(Vector2)}.
	 * @param relative relative position
	 * @return absolute position of {@code relative}
	 */
	public Vector2 getAbsolutePosition(Vector2 relative) {
		output.set(relative);
		output.add(halfSize, -1);

		// Scale to absolute coordinates
		output.setX(output.getX() / unitPixels);
		output.setY(output.getY() / -unitPixels);

		output.add(position); // Position relative to origin

		return output;
	}
	/**
	 * Calculates and returns the relative position of an absolute position vector.
	 * The returned vector is shared by all invocations of this method and {@link #getAbsolutePosition(Vector2)}.
	 * @param absolute absolute position
	 * @return position of {@code absolute} relative to this camera
	 */
	public Vector2 getRelativePosition(Vector2 absolute) {
		output.set(absolute);
		output.add(position, -1); // Position relative to camera

		// Scale to pixels
		output.setX(output.getX() * unitPixels);
		output.setY(output.getY() * -unitPixels);

		output.add(halfSize);  // Position relative to display

		return output;
	}

	/** @return camera position */
	public Vector2 getPosition() {
		return position;
	}
	/** @param position camera position */
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	/**
	 * @param width render medium width
	 * @param height render medium height
	 */
	public void setSize(double width, double height) {
		halfSize.setX(width);
		halfSize.setY(height);
		halfSize.scale(.5);
	}
}
