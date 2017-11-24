package dev.kkorolyov.pancake.skillet.ui.attribute

import dev.kkorolyov.pancake.skillet.decorator.content
import dev.kkorolyov.pancake.skillet.decorator.graphic
import dev.kkorolyov.pancake.skillet.decorator.styleClass
import dev.kkorolyov.pancake.skillet.decorator.tooltip
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox
import kotlin.collections.MutableMap.MutableEntry

/**
 * Displays maps.
 */
object MapDisplayer : Displayer<Map<String, Any>>(Map::class) {
	override fun display(attribute: MutableEntry<String, Map<String, Any>>): Node =
			TitledPane()
					.styleClass("attribute")
					.graphic(Label(attribute.key)
							.styleClass("attribute-name")
							.tooltip(getTooltipText()))
					.content(VBox(*attribute.value.entries
							.map { AutoDisplayer.display(it as MutableEntry) }
							.toTypedArray())
							.styleClass("attribute-content"))
}
