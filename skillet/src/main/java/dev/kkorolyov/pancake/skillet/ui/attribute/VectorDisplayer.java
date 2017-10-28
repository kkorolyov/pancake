package dev.kkorolyov.pancake.skillet.ui.attribute;

import dev.kkorolyov.pancake.platform.math.Vector;

import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static dev.kkorolyov.pancake.skillet.decorator.UIDecorator.decorate;

/**
 * Displays vectors.
 */
public class VectorDisplayer extends Displayer<Vector> {
	/**
	 * Constructs a new vector displayer.
	 */
	protected VectorDisplayer() {
		super(Vector.class);
	}

	@Override
	public Node display(Entry<String, Vector> entry) {
		return simpleDisplay(entry.getKey(),
				new HBox(
						buildSpinner(entry.getValue(), Vector::getX, Vector::setX),
						buildSpinner(entry.getValue(), Vector::getY, Vector::setY),
						buildSpinner(entry.getValue(), Vector::getZ, Vector::setZ)
				));
	}
	private static Spinner<Double> buildSpinner(Vector vector, Function<Vector, Float> extractor, BiConsumer<Vector, Float> applier) {
		return decorate(new Spinner<Double>(-Float.MAX_VALUE, Float.MAX_VALUE, extractor.apply(vector), .1))
				.patterns(NumberDisplayer.SEMI_NUMBER_PATTERN, NumberDisplayer.NUMBER_PATTERN)
				.press(10, 1, KeyCombination.SHIFT_DOWN)
				.change(Spinner::valueProperty,
						(target, oldValue, newValue) -> applier.accept(vector, newValue.floatValue()))
				.get();
	}
}
