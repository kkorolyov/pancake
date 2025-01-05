package dev.kkorolyov.pancake.input.control

import dev.kkorolyov.pancake.input.event.InputEvent
import dev.kkorolyov.pancake.input.event.KeyEvent
import dev.kkorolyov.pancake.input.event.StateEvent
import dev.kkorolyov.pancake.platform.action.Action

/**
 * Returns [action] for [KeyEvent]s matching [scanCode] and [state].
 */
data class KeyControl(var scanCode: Int, var state: StateEvent.State, var action: Action) : Control {
	override fun invoke(event: InputEvent): Action? = when (event) {
		is KeyEvent -> if (event.scanCode == scanCode && event.state == state) action else null
		else -> null
	}
}
