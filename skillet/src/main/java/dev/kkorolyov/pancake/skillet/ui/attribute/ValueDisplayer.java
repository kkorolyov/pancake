package dev.kkorolyov.pancake.skillet.ui.attribute;

import dev.kkorolyov.pancake.muffin.data.type.Attribute;
import dev.kkorolyov.pancake.skillet.utility.decorator.UIDecorator;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Displays an attribute according to its value.
 */
public abstract class ValueDisplayer {
	protected Node simpleDisplay(String name, String tooltip, Node value) {
		Label label = tooltip(tooltip,
				new Label(name + ": "));

		BorderPane pane = new BorderPane();
		pane.setLeft(label);
		pane.setRight(value);
		BorderPane.setAlignment(label, Pos.CENTER);
		BorderPane.setAlignment(value, Pos.CENTER);

		return pane;
	}

	protected <N extends Node> N tooltip(String tooltip, N value) {
		return UIDecorator.decorate(value)
				.tooltip(tooltip + " attribute")
				.get();
	}

	/** @return representative display of {@code attribute} */
	public abstract Node display(Attribute attribute);

	/** @return {@code true} if able to display attribute with value of type {@code c} */
	public abstract boolean accepts(Class<?> c);
}
