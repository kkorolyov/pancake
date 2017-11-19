package dev.kkorolyov.pancake.skillet.decorator

import javafx.scene.Node
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Labeled

/** @param graphic labeled graphic */
fun <T : Labeled> T.graphic(graphic: Node): T {
	this.graphic = graphic
	return this
}
/** @param contentDisplay content display style */
fun <T : Labeled> T.contentDisplay(contentDisplay: ContentDisplay): T {
	this.contentDisplay = contentDisplay
	return this
}
