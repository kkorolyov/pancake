package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import javafx.collections.ObservableList
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.RowConstraints

/**
 * Prints this vector's components in a UI-suitable format.
 */
fun Vector2.prettyPrint(): String = String.format("(%.3f, %.3f)", x, y)

/**
 * Prints this vector's components in a UI-suitable format.
 */
fun Vector3.prettyPrint(): String = String.format("(%.3f, %.3f, %.3f)", x, y, z)

fun GridPane.colRatios(vararg values: Number?) {
	ratios(columnConstraints, ::ColumnConstraints, ColumnConstraints::setPercentWidth, *values)
}

fun GridPane.rowRatios(vararg values: Number?) {
	ratios(rowConstraints, ::RowConstraints, RowConstraints::setPercentHeight, *values)
}

private fun <T> ratios(list: ObservableList<T>, creator: () -> T, setter: (T, Double) -> Unit, vararg values: Number?) {
	val total = values.filterNotNull().sumOf { it.toDouble() }

	list.setAll(
		values.map { it?.toDouble()?.div(total)?.times(100) }.map { creator().apply { it?.let { setter(this, it) } } }
	)
}
