package dev.kkorolyov.pancake.skillet.decorator

import javafx.scene.control.MenuItem

/**
 * Sets the item procedure.
 * attached item procedure
 */
fun <T : MenuItem> T.action(procedure: () -> Unit): T {
	setOnAction { procedure() }
	return this
}
