package dev.kkorolyov.pancake.platform.media.graphic;

import dev.kkorolyov.pancake.platform.service.RenderMedium;

/**
 * Something drawable on a {@link RenderMedium}.
 */
public interface Renderable {
	/**
	 * Renders the current state of {@code this} on the associated {@link RenderMedium} according to {@code transform}.
	 * @param transform rendering configuration
	 */
	void render(RenderTransform transform);
}
