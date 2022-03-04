package dev.kkorolyov.pancake.input.jfx

import dev.kkorolyov.pancake.input.common.Compensated
import dev.kkorolyov.pancake.input.common.Reaction
import dev.kkorolyov.pancake.platform.action.Action
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent

/**
 * Returns a reaction invoking the reaction of the first branch of [branches] matching an event's [KeyEvent.code].
 */
fun whenCode(vararg branches: Pair<KeyCode, Reaction<in KeyEvent>>): Reaction<KeyEvent> = Reaction {
	branches.asSequence()
		.filter { (first) -> first == it.code }
		.map { (_, second) -> second(it) }
		.firstOrNull()
}

/**
 * Returns a reaction returning the main value of [compensated] on press event, and its compensating value on release event.
 */
fun keyToggle(compensated: Compensated<Action>): Reaction<KeyEvent> = Reaction {
	when (it.eventType) {
		KeyEvent.KEY_PRESSED -> compensated.get()
		KeyEvent.KEY_RELEASED -> compensated.compensate()
		else -> null
	}
}

/**
 * Returns a reaction returning the main value of [compensated] on press event, and its compensating value on release event.
 */
fun mouseToggle(compensated: Compensated<Action>): Reaction<MouseEvent> = Reaction {
	when (it.eventType) {
		MouseEvent.MOUSE_PRESSED -> compensated.get()
		MouseEvent.MOUSE_RELEASED -> compensated.compensate()
		else -> null
	}
}
