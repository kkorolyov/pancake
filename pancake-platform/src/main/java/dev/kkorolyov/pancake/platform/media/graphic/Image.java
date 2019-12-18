package dev.kkorolyov.pancake.platform.media.graphic;

/**
 * A renderable static image.
 */
public abstract class Image implements Renderable {
	private Viewport viewport = new Viewport(1, 1);

	/** @return viewport scoping rendered portion of this image */
	public Viewport getViewport() {
		return viewport;
	}
	/** @param viewport viewport scoping rendered portion of this image */
	public Image setViewport(Viewport viewport) {
		this.viewport = viewport;
		return this;
	}
}
