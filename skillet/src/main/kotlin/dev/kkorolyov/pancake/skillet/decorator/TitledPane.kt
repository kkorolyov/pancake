package dev.kkorolyov.pancake.skillet.decorator

import javafx.scene.Node
import javafx.scene.control.TitledPane

/** @param content pane content */
fun <T : TitledPane> T.content(content: Node): T {
	this.content = content
	return this
}
