package dev.kkorolyov.pancake.skillet.ui.attribute.strategy;

import dev.kkorolyov.pancake.platform.storage.Attribute;
import dev.kkorolyov.pancake.platform.storage.Attribute.AttributeChangeEvent;
import dev.kkorolyov.pancake.skillet.ui.attribute.ValueDisplayer;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static dev.kkorolyov.pancake.skillet.UIDecorator.decorate;

/**
 * Displays attributes with a numerical value.
 */
public class NumberDisplayer extends ValueDisplayer {
	@Override
	public Node display(Attribute attribute) {
		TextField node = new TextField(attribute.getValue().toString());

		attribute.register((target, event) -> {
			if (AttributeChangeEvent.VALUE == event) node.setText(attribute.getValue().toString());
		});
		return simpleDisplay(attribute.getName(),
				"Number",
				decorate(node)
						.press(buildProcedureMap(attribute))
						.change(TextInputControl::textProperty,
								(target, oldValue, newValue) -> {
									if (!isSemiNumber(newValue)) target.setText(oldValue);

									if (isNumber(target.getText())) attribute.setValue(new BigDecimal(target.getText()));
									else if (target.getText().isEmpty()) attribute.setValue(new BigDecimal("0"));
								})
						.get());
	}
	private Map<KeyCombination, Runnable> buildProcedureMap(Attribute attribute) {
		Map<KeyCombination, Runnable> procedureMap = new HashMap<>();
		procedureMap.put(new KeyCodeCombination(KeyCode.UP), () -> changeValue(attribute, "1"));
		procedureMap.put(new KeyCodeCombination(KeyCode.DOWN), () -> changeValue(attribute, "-1"));
		procedureMap.put(new KeyCodeCombination(KeyCode.UP, KeyCodeCombination.SHIFT_DOWN), () -> changeValue(attribute, ".1"));
		procedureMap.put(new KeyCodeCombination(KeyCode.DOWN, KeyCodeCombination.SHIFT_DOWN), () -> changeValue(attribute, "-.1"));

		return procedureMap;
	}
	private static void changeValue(Attribute attribute, String delta) {
		attribute.setValue(attribute.getValue(BigDecimal.class)
				.add(new BigDecimal(delta)));
	}

	private static boolean isNumber(String s) {
		return s.matches("[+-]?(\\d*\\.\\d+|\\d+)");
	}
	private static boolean isSemiNumber(String s) {	// valid number, dot, or empty string
		return s.matches("[+-]?(\\d*\\.\\d*|\\d*)");
	}

	@Override
	public boolean accepts(Class<?> c) {
		return BigDecimal.class.isAssignableFrom(c);
	}
}
