package dev.kkorolyov.pancake.skillet.ui.attribute

import dev.kkorolyov.pancake.skillet.decorator.change
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.control.TextInputControl
import java.net.URI
import kotlin.collections.MutableMap.MutableEntry

object URIDisplayer : Displayer<URI>(URI::class) {
	override fun display(attribute: MutableEntry<String, URI>): Node =
			// TODO
			simpleDisplay(attribute.key,
					TextField(attribute.value.toString())
							.change(TextInputControl::textProperty, {_, _, newValue ->
								attribute.setValue(URI(newValue))
							}))
}
