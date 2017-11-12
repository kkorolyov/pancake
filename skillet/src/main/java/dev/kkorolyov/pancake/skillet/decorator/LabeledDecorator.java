package dev.kkorolyov.pancake.skillet.decorator;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;

/**
 * Decorates {@link Labeled} implementors.
 */
public class LabeledDecorator<T extends Labeled, D extends LabeledDecorator<T, D>> extends RegionDecorator<T, D> {
	protected LabeledDecorator(T object) {
		super(object);
	}

	/**
	 * Sets the graphic.
	 * @param graphic set graphic
	 * @return {@code this}
	 */
	public D graphic(Node graphic) {
		object.setGraphic(graphic);
		return (D) this;
	}

	/**
	 * Sets the content display property.
	 * @param contentDisplay set content display property
	 * @return {@code this}
	 */
	public D contentDisplay(ContentDisplay contentDisplay) {
		object.setContentDisplay(contentDisplay);
		return (D) this;
	}
}
