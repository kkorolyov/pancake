package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.media.Renderable;

import javafx.scene.canvas.GraphicsContext;

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
	public void render(GraphicsContext g, Vector position) {
		delegate.render(g, position);
	}
}
