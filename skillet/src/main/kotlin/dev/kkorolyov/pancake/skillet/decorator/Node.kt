package dev.kkorolyov.pancake.skillet.decorator

import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.control.ContextMenu
import javafx.scene.control.Tooltip
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.ScrollEvent

/** @param id element ID */
fun <T : Node> T.id(id: String): T {
	this.id = id
	return this
}
/** @param classes element styling classes */
fun <T : Node> T.styleClass(vararg classes: String): T {
	styleClass.addAll(classes)
	return this
}

/** @param tooltip tooltip text to attach */
fun <T : Node> T.tooltip(tooltip: String): T {
	Tooltip.install(this, Tooltip(tooltip))
	return this
}

/**
 * Sets the mouse clicked procedure.
 * @param clicks number of clicks required to invoke `procedure`
 * @param procedure attached click procedure, invoked with receiver
 */
fun <T : Node> T.click(clicks: Int = 1, procedure: (T) -> Unit): T {
	setOnMouseClicked { if (it.clickCount == clicks) procedure(this) }
	return this
}

/**
 * Sets the key press procedure.
 * @param keys set of keys which invoke `procedure`
 * @param procedure attached press procedure, invoked with receiver
 */
fun <T : Node> T.press(vararg keys: KeyCode, procedure: (T) -> Unit): T =
		press(*keys.map { key -> KeyCodeCombination(key) }.toTypedArray(), procedure = procedure)
/**
 * Sets the key press procedure.
 * @param keyCombinations set of key combinations which invoke `procedure`
 * @param procedure attached press procedure, invoked with receiver
 */
fun <T : Node> T.press(vararg keyCombinations: KeyCombination, procedure: (T) -> Unit): T {
	setOnKeyPressed { if (keyCombinations.any { keyCombination -> keyCombination.match(it) }) procedure(this) }
	return this
}
/**
 * Sets multiple key press procedures.
 * @param keyProcedures map of key combinations to procedures invoked with receiver
 */
fun <T : Node> T.press(keyProcedures: Map<out KeyCombination, (T) -> Unit>): T {
	setOnKeyPressed {
		keyProcedures.entries.firstOrNull { entry -> entry.key.match(it) }
				?.value?.invoke(this)
	}
	return this
}

/**
 * Sets the scroll event handler.
 * @param eventHandler scroll event handler
 */
fun <T : Node> T.scroll(eventHandler: (ScrollEvent) -> Unit): T {
	setOnScroll(eventHandler)
	return this
}

/**
 * Sets a property value.
 * @param propertyFunction retrieves property from receiver
 * @param value new property value
 */
fun <T : Node, V> T.property(propertyFunction: (T) -> Property<V>, value: V): T {
	propertyFunction(this).value = value
	return this
}

/**
 * Adds a change listener to an observable value.
 * @param observableFunction retrieves observable value from receiver
 * @param listener attached change listener
 */
fun <T : Node, V> T.change(observableFunction: (T) -> ObservableValue<V>, listener: (T, V, V) -> Unit): T {
	observableFunction(this).addListener { _, oldValue, newValue ->
		listener(this, oldValue, newValue)
	}
	return this
}

/**
 * Applies a binding on a property.
 * @param propertyFunction retrieves property from receiver
 * @param bound bound property
 */
fun <T : Node, V> T.bind(propertyFunction: (T) -> Property<V>, bound: ObservableValue<V>): T {
	propertyFunction(this).bind(bound)
	return this
}

/**
 * Sets the attached context menu.
 * @param contextMenuSupplier supplies context menu on demand
 */
fun <T : Node, C : ContextMenu> T.contextMenu(contextMenuSupplier: () -> C): T {
	setOnContextMenuRequested {
		contextMenuSupplier().show(this, it.screenX, it.screenY)
		it.consume()
	}
	return this
}
