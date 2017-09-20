package dev.kkorolyov.pancake.skillet.decorator;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;

/**
 * Decorates {@link TitledPane}s.
 */
public class TitledPaneDecorator<T extends TitledPane> extends LabeledDecorator<T, TitledPaneDecorator<T>> {
	/**
	 * Constructs a new TitledPane decorator.
	 * @param object decorated object
	 */
	public TitledPaneDecorator(T object) {
		super(object);
	}

	/**
	 * Sets the content
	 * @param content set content
	 * @return {@code this}
	 */
	public TitledPaneDecorator<T> content(Node content) {
		object.setContent(content);
		return this;
	}
}
