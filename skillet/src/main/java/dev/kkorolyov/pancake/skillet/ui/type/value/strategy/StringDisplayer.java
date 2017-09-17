package dev.kkorolyov.pancake.skillet.ui.type.value.strategy;

import dev.kkorolyov.pancake.muffin.data.type.Attribute;
import dev.kkorolyov.pancake.skillet.ui.type.value.ValueDisplayer;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.decorate;

/**
 * Displays attributes with a string value.
 */
public class StringDisplayer extends ValueDisplayer {
	@Override
	public Node display(Attribute attribute) {
		return simpleDisplay(attribute.getName(),
				"Text",
				decorate(new TextField(attribute.getValue().toString()))
						.change((target, oldValue, newValue) -> {
									if (!newValue.equals(oldValue)) attribute.setValue(newValue);
								},
								TextInputControl::textProperty)
						.get());
	}

	@Override
	public boolean accepts(Class<?> c) {
		return String.class.isAssignableFrom(c);
	}
}
