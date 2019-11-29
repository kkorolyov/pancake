package dev.kkorolyov.pancake.platform.media.graphic;

import dev.kkorolyov.pancake.platform.media.Camera;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Box;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Text;

/**
 * Provides for rendering various graphical artifacts.
 */
public interface RenderMedium {
	/** @return camera representing current view into this medium */
	Camera getCamera();

	/**
	 * @param uri image source URI
	 * @return instance of image source at {@code uri} renderable on this medium
	 */
	Image getImage(String uri);

	/** @return box instance renderable on this medium */
	Box getBox();
	/** @return text instance renderable on this medium */
	Text getText();

	/**
	 * Clears all rendered artifacts on this medium.
	 */
	void clear();
}
