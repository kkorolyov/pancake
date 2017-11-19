package dev.kkorolyov.pancake.skillet.decorator

import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.control.Tab

/** @param id element ID */
fun <T : Tab> T.id(id: String): T {
	this.id = id
	return this
}
/** @param classes element styling classes */
fun <T : Tab> T.styleClass(vararg classes: String): T {
	styleClass.addAll(classes)
	return this
}

/** @param graphic tab graphic */
fun <T : Tab> T.graphic(graphic: Node): T {
	this.graphic = graphic
	return this
}
/** @param content tab content */
fun <T : Tab> T.content(content: Node): T {
	this.content = content
	return this
}

/**
 * Sets the tab close procedure.
 * @param procedure attached close procedure
 */
fun <T : Tab> T.close(procedure: () -> Unit): T {
	setOnClosed({ procedure() })
	return this
}

/**
 * Adds a change listener to an observable value.
 * @param observableFunction retrieves observable value from receiver
 * @param listener attached change listener
 */
fun <T : Tab, V> T.change(observableFunction: (T) -> ObservableValue<V>, listener: (T, V, V) -> Unit): T {
	observableFunction(this).addListener({ _, oldValue, newValue ->
		listener(this, oldValue, newValue)
	})
	return this
}
