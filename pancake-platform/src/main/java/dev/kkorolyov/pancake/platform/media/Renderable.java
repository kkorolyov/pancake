package dev.kkorolyov.pancake.platform.media;

import dev.kkorolyov.pancake.platform.math.Vector;

import javafx.scene.canvas.GraphicsContext;

/**
 * Something renderable by a {@link GraphicsContext}.
 */
public interface Renderable {
	/**
	 * Renders {@code this} at some position using a graphics context.
	 * @param g graphics context to use for rendering
	 * @param position render position in px from the top-left corner of the render medium
	 */
	void render(GraphicsContext g, Vector position);

	/** @return size in px */
	Vector size();
}
