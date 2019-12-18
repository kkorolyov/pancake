package dev.kkorolyov.pancake.skillet.ui.attribute

import dev.kkorolyov.pancake.platform.serialization.string.NumberStringSerializer
import dev.kkorolyov.pancake.skillet.patterns
import dev.kkorolyov.pancake.skillet.press
import javafx.scene.Node
import javafx.scene.control.Spinner
import java.math.BigDecimal
import java.util.regex.Pattern
import kotlin.collections.MutableMap.MutableEntry

/**
 * Displays numerical values.
 */
object NumberDisplayer : Displayer<BigDecimal>(BigDecimal::class) {
	val NUMBER_PATTERN: Pattern = Pattern.compile(NumberStringSerializer().pattern())
	val SEMI_NUMBER_PATTERN: Pattern = Pattern.compile(NUMBER_PATTERN.pattern().replace("d+", "d*"))

	override fun display(attribute: MutableEntry<String, BigDecimal>): Node =
			simpleDisplay(attribute.key,
					Spinner<Double>(-Double.MAX_VALUE, Double.MAX_VALUE, attribute.value.toDouble(), .1).apply {
						patterns(SEMI_NUMBER_PATTERN, NUMBER_PATTERN)
						press(10, 1)
						valueProperty().addListener { _, _, newValue ->
							attribute.setValue(BigDecimal(newValue))
						}
					})
}
