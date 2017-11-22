package dev.kkorolyov.pancake.skillet.ui.attribute

import dev.kkorolyov.pancake.skillet.decorator.change
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.control.TextInputControl
import kotlin.collections.MutableMap.MutableEntry

object StringDisplayer : Displayer<String>(String::class) {
	override fun display(attribute: MutableEntry<String, String>): Node =
			simpleDisplay(attribute.key,
					TextField(attribute.value)
							.change(TextInputControl::textProperty, { _, _, newValue ->
								attribute.setValue(newValue)
							}))
}
