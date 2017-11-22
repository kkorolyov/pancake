package dev.kkorolyov.pancake.skillet.ui.attribute

import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.skillet.decorator.change
import dev.kkorolyov.pancake.skillet.decorator.patterns
import dev.kkorolyov.pancake.skillet.decorator.press
import javafx.scene.Node
import javafx.scene.control.Spinner
import javafx.scene.layout.HBox
import kotlin.collections.MutableMap.MutableEntry

object VectorDisplayer : Displayer<Vector>(Vector::class) {
	override fun display(attribute: MutableEntry<String, Vector>): Node =
			simpleDisplay(attribute.key,
					HBox(
							buildSpinner(attribute.value, Vector::getX, Vector::setX),
							buildSpinner(attribute.value, Vector::getY, Vector::setY),
							buildSpinner(attribute.value, Vector::getZ, Vector::setZ)
					))

	private fun buildSpinner(vector: Vector, extractor: (Vector) -> Float, applier: (Vector, Float) -> Unit): Spinner<Double> =
			Spinner<Double>(-Float.MAX_VALUE.toDouble(), Float.MAX_VALUE.toDouble(), extractor(vector).toDouble(), .1)
					.patterns(NumberDisplayer.SEMI_NUMBER_PATTERN, NumberDisplayer.NUMBER_PATTERN)
					.press(10, 1)
					.change(Spinner<Double>::valueProperty, {_, _, newValue ->
						applier(vector, newValue.toFloat())
					})
}
