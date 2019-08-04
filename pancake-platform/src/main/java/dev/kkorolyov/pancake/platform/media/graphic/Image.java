package dev.kkorolyov.pancake.platform.media.graphic;

/**
 * A renderable static image.
 */
public abstract class Image implements Renderable {
	private Viewport viewport;

	/** @return viewport scoping rendered portion of this image, or {@code null} */
	public Viewport getViewport() {
		return viewport;
	}
	/** @param viewport viewport scoping rendered portion of this image */
	public Image setViewport(Viewport viewport) {
		this.viewport = viewport;
		return this;
	}
}
