package dev.kkorolyov.pancake.skillet.ui.attribute;

import dev.kkorolyov.pancake.platform.serialization.string.NumberStringSerializer;

import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCombination;
import java.math.BigDecimal;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import static dev.kkorolyov.pancake.skillet.decorator.UIDecorator.decorate;

/**
 * Displays numerical values.
 */
public class NumberDisplayer extends Displayer<BigDecimal> {
	public static final Pattern NUMBER_PATTERN = Pattern.compile(new NumberStringSerializer().pattern());
	public static final Pattern SEMI_NUMBER_PATTERN = Pattern.compile(NUMBER_PATTERN.pattern().replace("d+", "d*"));

	/**
	 * Constructs a new number displayer.
	 */
	public NumberDisplayer() {
		super(BigDecimal.class);
	}

	@Override
	public Node display(Entry<String, BigDecimal> entry) {
		return simpleDisplay(entry.getKey(),
				decorate(new Spinner<Double>(-Float.MAX_VALUE, Float.MAX_VALUE, entry.getValue().floatValue(), .1))
						.patterns(SEMI_NUMBER_PATTERN, NUMBER_PATTERN)
						.press(10, 1, KeyCombination.SHIFT_DOWN)
						.change(Spinner::valueProperty,
								(target, oldValue, newValue) -> entry.setValue(new BigDecimal(newValue)))
						.get());
	}
}
