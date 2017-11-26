package dev.kkorolyov.pancake.skillet.ui.attribute

import javafx.scene.Node
import javafx.scene.control.TextField
import kotlin.collections.MutableMap.MutableEntry

object StringDisplayer : Displayer<String>(String::class) {
	override fun display(attribute: MutableEntry<String, String>): Node =
			simpleDisplay(attribute.key,
					TextField(attribute.value).apply {
						textProperty().addListener {_, _, newValue ->
							attribute.setValue(newValue)
						}
					})
}
