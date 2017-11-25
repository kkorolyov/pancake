package dev.kkorolyov.pancake.skillet.decorator

import javafx.scene.control.ButtonBase

/**
 * Sets the click procedure.
 * @param procedure attached click procedure
 */
fun <T : ButtonBase> T.action(procedure: () -> Unit): T {
	setOnAction { _ -> procedure() }
	return this
}

/**
 * Sets the disabled status.
 * @param disable buttons disabled status
 */
fun <T : ButtonBase> T.disabled(disable: Boolean): T {
	isDisable = disable
	return this
}
