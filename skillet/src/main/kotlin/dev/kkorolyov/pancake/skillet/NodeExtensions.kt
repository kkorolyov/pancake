package dev.kkorolyov.pancake.skillet

import javafx.event.ActionEvent
import javafx.scene.Node
import javafx.scene.control.ContextMenu
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.control.ScrollPane
import javafx.scene.control.Spinner
import javafx.scene.control.Tooltip
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import java.util.regex.Pattern

// Node
/** @param tooltip tooltip text to attach */
fun <T : Node> T.tooltip(tooltip: String) {
	Tooltip.install(this, Tooltip(tooltip))
}

/**
 * Sets the mouse click handler.
 * @param clicks number of clicks required to invoke `handler`
 * @param handler attached click event handler
 */
fun <T : Node> T.click(clicks: Int = 1, handler: (MouseEvent) -> Unit) {
	setOnMouseClicked { if (it.clickCount == clicks) handler(it) }
}

/**
 * Sets the key press handler.
 * @param keys set of keys which invoke `handler`
 * @param handler attached press event handler
 */
fun <T : Node> T.press(vararg keys: KeyCode, handler: (KeyEvent) -> Unit) {
	press(*keys.map { key -> KeyCodeCombination(key) }.toTypedArray(), handler = handler)
}
/**
 * Sets the key press handler.
 * @param keyCombinations set of key combinations which invoke `handler`
 * @param handler attached press event handler
 */
fun <T : Node> T.press(vararg keyCombinations: KeyCombination, handler: (KeyEvent) -> Unit) {
	setOnKeyPressed { if (keyCombinations.any { keyCombination -> keyCombination.match(it) }) handler(it) }
}
/**
 * Sets multiple key press handlers.
 * @param keyProcedures map of key combinations to invoked event handlers
 */
fun <T : Node> T.press(keyProcedures: Map<out KeyCombination, (KeyEvent) -> Unit>) {
	setOnKeyPressed {
		keyProcedures.entries.firstOrNull { entry -> entry.key.match(it) }
				?.value?.invoke(it)
	}
}

/**
 * Sets the scroll event handler.
 * @param handler attached scroll event handler
 */
fun <T : Node> T.scroll(handler: (ScrollEvent) -> Unit) {
	setOnScroll {
		handler(it)
		it.consume()
	}
}

/**
 * Sets the attached context menu.
 * @param contextMenuSupplier supplies context menu on demand
 */
fun <T : Node, C : ContextMenu> T.contextMenu(contextMenuSupplier: () -> C) {
	setOnContextMenuRequested {
		contextMenuSupplier().show(this, it.screenX, it.screenY)
		it.consume()
	}
}

// ScrollPane
/**
 * Fits the width and height of content and sets minimum size to `0`.
 */
fun <T : ScrollPane> T.compact() {
	isFitToWidth = true
	isFitToHeight = true

	minWidth = 0.0
	minHeight = 0.0
}

// Spinner
/**
 * Makes this editable and sets regex patterns validating its values.
 * @param valid pattern which all entered text must match
 * @param committed pattern which entered and committed text must match
 */
fun <T : Spinner<*>> T.patterns(valid: Pattern, committed: Pattern) {
	isEditable = true
	editor.setOnAction {
		if (committed.matcher(editor.text).matches()) commitValue()
	}
	focusedProperty().addListener { _, _, newValue ->
		if (!newValue && committed.matcher(editor.text).matches()) commitValue()
	}
	with(editor) {
		textProperty().addListener {_, oldValue, newValue ->
			if (!valid.matcher(newValue).matches()) text = oldValue
		}
	}
}

/**
 * Sets the number of steps this is changed by when arrow keys or scroll events are applied to it.
 * @param standard standard number of steps to increment/decrement by
 * @param modified number of steps to increment/decrement by when `ALT` is also held
 */
fun <T : Spinner<*>> T.press(standard: Int, modified: Int) {
	editor.press(mapOf(
			KeyCodeCombination(KeyCode.UP) to { _ -> increment(standard) },
			KeyCodeCombination(KeyCode.DOWN) to { _ -> decrement(standard) },
			KeyCodeCombination(KeyCode.UP, KeyCombination.ALT_DOWN) to { _ -> increment(modified) },
			KeyCodeCombination(KeyCode.DOWN, KeyCombination.ALT_DOWN) to { _ -> decrement(modified) }
	))
	scroll {
		val steps = if (it.isAltDown) modified else standard

		if (it.deltaY < 0) decrement(steps)
		else increment(steps)
	}
}

// Menu
/**
 * Adds a menu item.
 * @param name menu item name
 * @param listener menu item action event listener
 */
fun <T : Menu> T.item(name: String, listener: (ActionEvent) -> Unit) {
	items.add(MenuItem(name).apply {
		setOnAction(listener)
	})
}

// ContextMenu
/**
 * Adds a menu item.
 * @param name menu item name
 * @param listener menu item action event listener
 */
fun <T : ContextMenu> T.item(name: String, listener: (ActionEvent) -> Unit) {
	items.add(MenuItem(name).apply {
		setOnAction(listener)
	})
}
