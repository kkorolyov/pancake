package dev.kkorolyov.pancake.skillet.ui.attribute;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import java.util.Map.Entry;

import static dev.kkorolyov.pancake.skillet.decorator.UIDecorator.decorate;

/**
 * Displays strings.
 */
public class StringDisplayer extends Displayer<String> {
	/**
	 * Constructs a new string displayer.
	 */
	public StringDisplayer() {
		super(String.class);
	}

	@Override
	public Node display(Entry<String, String> entry) {
		return simpleDisplay(entry.getKey(),
				decorate(new TextField(entry.getValue()))
						.change(TextInputControl::textProperty,
								(target, oldValue, newValue) -> {
									if (!newValue.equals(oldValue)) entry.setValue(newValue);
								})
						.get());
	}
}
