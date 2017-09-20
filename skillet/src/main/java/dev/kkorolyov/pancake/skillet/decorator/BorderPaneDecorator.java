package dev.kkorolyov.pancake.skillet.decorator;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * Decorates {@link BorderPane}s.
 */
public class BorderPaneDecorator<T extends BorderPane, D extends BorderPaneDecorator<T, D>> extends RegionDecorator<T, D> {
	/**
	 * Constructs a new BorderPane decorator.
	 * @param object decorated object
	 */
	public BorderPaneDecorator(T object) {
		super(object);
	}

	/**
	 * Sets left content.
	 * @param content set content
	 * @return {@code this}
	 */
	public D left(Node content) {
		object.setLeft(content);
		align(content);

		return (D) this;
	}
	/**
	 * Sets right content.
	 * @param content set content
	 * @return {@code this}
	 */
	public D right(Node content) {
		object.setRight(content);
		align(content);

		return (D) this;
	}
	/**
	 * Sets top content.
	 * @param content set content
	 * @return {@code this}
	 */
	public D top(Node content) {
		object.setTop(content);
		align(content);

		return (D) this;
	}
	/**
	 * Sets bottom content.
	 * @param content set content
	 * @return {@code this}
	 */
	public D bottom(Node content) {
		object.setBottom(content);
		align(content);

		return (D) this;
	}
	/**
	 * Sets center content.
	 * @param content set content
	 * @return {@code this}
	 */
	public D center(Node content) {
		object.setCenter(content);
		align(content);

		return (D) this;
	}

	private void align(Node content) {
		BorderPane.setAlignment(content, Pos.CENTER);
	}
}
