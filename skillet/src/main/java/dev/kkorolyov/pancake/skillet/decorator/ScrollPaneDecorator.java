package dev.kkorolyov.pancake.skillet.decorator;

import javafx.scene.control.ScrollPane;

/**
 * Decorates {@link ScrollPane}s.
 */
public class ScrollPaneDecorator<T extends ScrollPane> extends RegionDecorator<T, ScrollPaneDecorator<T>> {
	/**
	 * Constructs a new ScrollPane decorator.
	 * @param object decorated object
	 */
	public ScrollPaneDecorator(T object) {
		super(object);
	}

	/**
	 * Fits the width and height of content and sets the minimum size to {@code 0}.
	 * @return {@code this}
	 */
	public ScrollPaneDecorator<T> compact() {
		object.setFitToWidth(true);
		object.setFitToHeight(true);
		object.setMinSize(0, 0);

		return this;
	}
}