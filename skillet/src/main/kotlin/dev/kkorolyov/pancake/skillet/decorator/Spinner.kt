package dev.kkorolyov.pancake.skillet.decorator

import javafx.scene.Node
import javafx.scene.control.Spinner
import javafx.scene.control.TextInputControl
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import java.util.regex.Pattern

/**
 * Makes the `Spinner` editable and sets regex patterns validating its values.
 * @param valid pattern which all entered text must match
 * @param committed pattern which entered and committed text must match
 */
fun <T : Spinner<*>> T.patterns(valid: Pattern, committed: Pattern): T {
	isEditable = true
	editor.setOnAction {
		if (committed.matcher(editor.text).matches()) commitValue()
	}
	change(Node::focusedProperty) { target, _, newValue ->
		if (!newValue && committed.matcher(target.editor.text).matches()) target.commitValue()
	}
	editor.change(TextInputControl::textProperty) { target, oldValue, newValue ->
		if (!valid.matcher(newValue).matches()) target.text = oldValue
	}
	return this
}

/**
 * Sets the number of steps the `Spinner` is changed by when arrow keys or scroll events are applied to it.
 * @param standard standard number of steps to increment/decrement by
 * @param modified number of steps to increment/decrement by when `ALT` is also held
 */
fun <T : Spinner<*>> T.press(standard: Int, modified: Int): T {
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
	return this
}
