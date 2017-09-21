package dev.kkorolyov.pancake.skillet.ui.attribute;

import dev.kkorolyov.pancake.storage.Attribute;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import static dev.kkorolyov.pancake.skillet.UIDecorator.decorate;

/**
 * Displays an attribute according to its value.
 */
public abstract class ValueDisplayer {
	protected Node simpleDisplay(String name, String tooltip, Node value) {
		return decorate(new BorderPane())
				.left(decorate(new Label(name + ": "))
						.styleClass("attribute-name")
						.tooltip(tooltip(tooltip))  // Maximum clarity
						.get())
				.right(value)
				.get();
	}

	protected String tooltip(String tooltip) {
		return tooltip + " attribute";
	}

	/** @return representative display of {@code attribute} */
	public abstract Node display(Attribute attribute);

	/** @return {@code true} if able to display attribute with value of type {@code c} */
	public abstract boolean accepts(Class<?> c);
}
