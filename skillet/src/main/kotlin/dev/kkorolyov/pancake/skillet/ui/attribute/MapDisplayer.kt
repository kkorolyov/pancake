package dev.kkorolyov.pancake.skillet.ui.attribute

import dev.kkorolyov.pancake.skillet.tooltip
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox
import kotlin.collections.MutableMap.MutableEntry

/**
 * Displays maps.
 */
object MapDisplayer : Displayer<MutableMap<String, Any>>(MutableMap::class) {
	override fun display(attribute: MutableEntry<String, MutableMap<String, Any>>): Node =
			TitledPane().apply {
				styleClass += "attribute"
				graphic = Label(attribute.key).apply {
					styleClass += "attribute-name"
					tooltip(getTooltipText())
				}
				content = VBox().apply {
					styleClass += "attribute-content"
					children += attribute.value.entries.map(AutoDisplayer::display)
				}
			}
}
