package dev.kkorolyov.pancake.platform.media.graphic;

import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * Something drawable on a {@link RenderMedium}.
 */
public interface Renderable {
	/**
	 * Renders the current state of {@code this} on the associated {@link RenderMedium} at {@code position}.
	 * @param position position to render the center of {@code this} at
	 */
	void render(Vector position);
}
