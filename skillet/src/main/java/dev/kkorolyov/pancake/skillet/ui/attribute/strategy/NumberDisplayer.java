package dev.kkorolyov.pancake.skillet.ui.attribute.strategy;

import dev.kkorolyov.pancake.muffin.data.type.Attribute;
import dev.kkorolyov.pancake.skillet.ui.attribute.ValueDisplayer;
import dev.kkorolyov.pancake.skillet.utility.Data;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import java.text.NumberFormat;

import static dev.kkorolyov.pancake.skillet.utility.decorator.UIDecorator.decorate;

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
									if (!Data.isSemiNumber(newValue)) target.setText(oldValue);

									if (!target.getText().equals(oldValue)) {
										attribute.setValue(Data.isNumber(target.getText())
												? NumberFormat.getInstance().parse(target.getText())
												: 0);
									}
								})
						.get());
	}

	@Override
	public boolean accepts(Class<?> c) {
		return Number.class.isAssignableFrom(c);
	}
}
