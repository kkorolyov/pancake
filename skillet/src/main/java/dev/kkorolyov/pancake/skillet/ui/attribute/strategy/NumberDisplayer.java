package dev.kkorolyov.pancake.skillet.ui.attribute.strategy;

import dev.kkorolyov.pancake.platform.storage.Attribute;
import dev.kkorolyov.pancake.skillet.ui.attribute.ValueDisplayer;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import java.text.NumberFormat;

import static dev.kkorolyov.pancake.skillet.UIDecorator.decorate;

/**
 * Displays attributes with a numerical value.
 */
public class NumberDisplayer extends ValueDisplayer {
	@Override
	public Node display(Attribute attribute) {
		return simpleDisplay(attribute.getName(),
				"Number",
				decorate(new TextField(attribute.getValue().toString()))
						.change(TextInputControl::textProperty,
								(target, oldValue, newValue) -> {
									if (!isSemiNumber(newValue)) target.setText(oldValue);

									if (!target.getText().equals(oldValue)) {
										attribute.setValue(isNumber(target.getText())
												? NumberFormat.getInstance().parse(target.getText())
												: 0);
									}
								})
						.get());
	}
	private static boolean isNumber(String s) {
		return s.matches("[+-]?(\\d*\\.\\d+|\\d+\\.\\d*|\\d+)");
	}
	private static boolean isSemiNumber(String s) {	// valid number, dot, or empty string
		return s.matches("[+-]?(\\d*\\.\\d*|\\d*)");
	}

	@Override
	public boolean accepts(Class<?> c) {
		return Number.class.isAssignableFrom(c);
	}
}
