package dev.kkorolyov.pancake.skillet.ui.attribute

import dev.kkorolyov.pancake.platform.serialization.string.NumberStringSerializer
import dev.kkorolyov.pancake.skillet.decorator.change
import dev.kkorolyov.pancake.skillet.decorator.patterns
import dev.kkorolyov.pancake.skillet.decorator.press
import javafx.scene.Node
import javafx.scene.control.Spinner
import java.util.regex.Pattern
import kotlin.collections.MutableMap.MutableEntry

/**
 * Displays numerical values.
 */
object NumberDisplayer : Displayer<Number>(Number::class) {
	val NUMBER_PATTERN: Pattern = Pattern.compile(NumberStringSerializer().pattern())
	val SEMI_NUMBER_PATTERN: Pattern = Pattern.compile(NUMBER_PATTERN.pattern().replace("d+", "d*"))

	override fun display(attribute: MutableEntry<String, Number>): Node =
			simpleDisplay(attribute.key,
					Spinner<Double>(-Float.MAX_VALUE.toDouble(), Float.MAX_VALUE.toDouble(), attribute.value.toDouble(), .1)
							.patterns(SEMI_NUMBER_PATTERN, NUMBER_PATTERN)
							.press(10, 1)
							.change(Spinner<Double>::valueProperty, { _, _, newValue ->
								attribute.setValue(newValue)
							}))
}
