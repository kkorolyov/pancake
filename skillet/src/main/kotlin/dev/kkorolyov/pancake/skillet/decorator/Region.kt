package dev.kkorolyov.pancake.skillet.decorator

import javafx.scene.layout.Region

/**
 * Sets minimum size.
 * @param minWidth minimum width
 * @param minHeight minimum height
 */
fun <T : Region> T.minSize(minWidth: Double? = null, minHeight: Double? = null): T {
	minWidth?.let { setMinWidth(it) }
	minHeight?.let { setMinHeight(it) }

	return this
}
/**
 * Sets maximum size.
 * @param maxWidth maximum width
 * @param maxHeight maximum height
 */
fun <T : Region> T.maxSize(maxWidth: Double? = null, maxHeight: Double? = null): T {
	maxWidth?.let { setMaxWidth(it) }
	maxHeight?.let { setMaxHeight(it) }

	return this
}
/**
 * Sets preferred size.
 * @param prefWidth preferred width
 * @param prefHeight preferred height
 */
fun <T : Region> T.prefSize(prefWidth: Double? = null, prefHeight: Double? = null): T {
	prefWidth?.let { setPrefWidth(it) }
	prefHeight?.let { setPrefHeight(it) }

	return this
}
