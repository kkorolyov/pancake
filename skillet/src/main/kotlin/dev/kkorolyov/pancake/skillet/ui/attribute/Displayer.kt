package dev.kkorolyov.pancake.skillet.ui.attribute

import dev.kkorolyov.pancake.skillet.decorator.tooltip
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import kotlin.collections.Map.Entry
import kotlin.collections.MutableMap.MutableEntry
import kotlin.reflect.KClass

/**
 * Displays an attribute.
 */
abstract class Displayer<T : Any>(private val acceptedType: KClass<in T>) {
	/**
	 * Generates a displayable representation of a attribute.
	 * @param attribute attribute to display
	 * @return representative display of attribute
	 */
	abstract fun display(attribute: MutableEntry<String, T>): Node

	fun accepts(attribute: Entry<String, *>): Boolean = acceptedType.isInstance(attribute.value)

	protected fun simpleDisplay(name: String, value: Node): Node =
			BorderPane().apply {
				left = Label("$name: ").apply {
					styleClass += "attribute-name"
					tooltip(getTooltipText())
				}
				right = value
				minWidth = 0.0
			}

	protected fun getTooltipText(): String = "${acceptedType.simpleName} attribute"
}
