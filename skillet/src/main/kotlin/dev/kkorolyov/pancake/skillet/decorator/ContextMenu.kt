package dev.kkorolyov.pancake.skillet.decorator

import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem

/**
 * Adds a menu item.
 * @param name menu item name
 * @param procedure menu item selection action
 */
fun <T : ContextMenu> T.item(name: String, procedure: () -> Unit): T {
	items.add(MenuItem(name)
			.action(procedure))
	return this
}
