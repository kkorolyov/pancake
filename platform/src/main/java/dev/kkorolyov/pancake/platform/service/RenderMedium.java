package dev.kkorolyov.pancake.platform.service;

import dev.kkorolyov.pancake.platform.media.Camera;
import dev.kkorolyov.pancake.platform.media.graphic.Image;
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
	 * Invokes a render action on this medium's rendering thread.
	 * @param renderAction render action to invoke
	 */
	void invoke(Runnable renderAction);
	/**
	 * Like {@link #invoke(Runnable)}, but fully clears the previous state of the render medium first.
	 * @see #invoke(Runnable)
	 */
	void clearInvoke(Runnable renderAction);
}
