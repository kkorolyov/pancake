package dev.kkorolyov.pancake.skillet.ui.attribute

import dev.kkorolyov.pancake.skillet.ResourceHandler
import dev.kkorolyov.pancake.skillet.tooltip
import javafx.scene.Node
import javafx.scene.control.Button
import java.io.File
import java.net.URI
import kotlin.collections.MutableMap.MutableEntry

object URIDisplayer : Displayer<URI>(URI::class) {
	override fun display(attribute: MutableEntry<String, URI>): Node =
			simpleDisplay(attribute.key,
					Button(attribute.value.toString()).apply {
						tooltip(text)
						setOnAction {
							ResourceHandler.chooseLoad(attribute.key, initialDirectory = File(attribute.value).absoluteFile.parentFile)?.let {
								attribute.setValue(it.toURI())
								text = attribute.value.toString()
								tooltip(text)
							}
						}
					})
}
