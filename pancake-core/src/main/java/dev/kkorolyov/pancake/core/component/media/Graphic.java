package dev.kkorolyov.pancake.core.component.media;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform;
import dev.kkorolyov.pancake.platform.media.graphic.Renderable;

/**
 * A swappable visual representation.
 */
public class Graphic implements Component, Renderable {
	private Renderable delegate;

	/**
	 * Constructs a new graphic.
	 * @param delegate delegate providing rendering logic
	 */
	public Graphic(Renderable delegate) {
		setDelegate(delegate);
	}

	/** @return delegate providing rendering logic */
	public Renderable getDelegate() {
		return delegate;
	}
	/**
	 * @param delegate delegate providing rendering logic
	 * @return {@code this}
	 */
	public Graphic setDelegate(Renderable delegate) {
		this.delegate = delegate;
		return this;
	}

	@Override
	public void render(RenderTransform transform) {
		delegate.render(transform);
	}
}
