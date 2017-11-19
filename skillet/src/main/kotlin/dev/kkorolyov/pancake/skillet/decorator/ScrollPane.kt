package dev.kkorolyov.pancake.skillet.decorator

import javafx.scene.control.ScrollPane

/**
 * Fits the width and height of content and sets minimum size to `0`.
 */
fun <T : ScrollPane> T.compact(): T {
	isFitToWidth = true
	isFitToHeight = true
	minSize(0.0, 0.0)

	return this
}
