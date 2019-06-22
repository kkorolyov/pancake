package dev.kkorolyov.pancake.platform.media.graphic;

/**
 * Provides for rendering various graphical artifacts.
 */
public interface RenderMedium {
	/**
	 * @param uri image source URI
	 * @return instance of image source at {@code uri} renderable on this medium
	 */
	Image getImage(String uri);
}
