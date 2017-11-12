package dev.kkorolyov.pancake.skillet.decorator;

import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Decorates {@link Spinner}s.
 */
public class SpinnerDecorator<T extends Spinner> extends RegionDecorator<T, SpinnerDecorator<T>> {
	protected SpinnerDecorator(T object) {
		super(object);
	}

	/**
	 * Makes the decorated spinner editable and sets regex patterns validating its values.
	 * @param valid pattern which all entered text must match
	 * @param committed pattern which entered and committed text must match
	 * @return {@code this}
	 */
	public SpinnerDecorator<T> patterns(Pattern valid, Pattern committed) {
		object.setEditable(true);
		object.getEditor().setOnAction(e -> {
			if (committed.matcher(object.getEditor().getText()).matches()) object.commitValue();
		});
		decorate(object)
				.change(Node::focusedProperty,
						(target, oldValue, newValue) -> {
							if (!newValue && committed.matcher(target.getEditor().getText()).matches()) target.commitValue();
						});
		decorate(object.getEditor())
				.change(TextInputControl::textProperty,
						(target, oldValue, newValue) -> {
							if (!valid.matcher(newValue).matches()) target.setText(oldValue);
						});
		return this;
	}

	/**
	 * Sets the number of steps the decorated spinner is changed by when arrow keys or scroll events are applied to it.
	 * @param standard standard number of steps to increment/decrement by
	 * @param modified number of steps to increment/decrement by when ALT is also held
	 * @return {@code this}
	 */
	public SpinnerDecorator<T> press(int standard, int modified) {
		decorate(object.getEditor())
				.press(buildSpinnerProcedures(standard, modified, KeyCombination.ALT_DOWN));
		decorate(object)
				.scroll(e -> {
					int steps = e.isAltDown() ? modified : standard;

					if (e.getDeltaY() < 0) { object.decrement(steps); } else object.increment(steps);
				});
		return this;
	}

	private Map<KeyCombination, Runnable> buildSpinnerProcedures(int standard, int modified, Modifier modifier) {
		Map<KeyCombination, Runnable> procedureMap = new HashMap<>();
		procedureMap.put(new KeyCodeCombination(KeyCode.UP), () -> object.increment(standard));
		procedureMap.put(new KeyCodeCombination(KeyCode.DOWN), () -> object.decrement(standard));
		procedureMap.put(new KeyCodeCombination(KeyCode.UP, modifier), () -> object.increment(modified));
		procedureMap.put(new KeyCodeCombination(KeyCode.DOWN, modifier), () -> object.decrement(modified));

		return procedureMap;
	}
}
