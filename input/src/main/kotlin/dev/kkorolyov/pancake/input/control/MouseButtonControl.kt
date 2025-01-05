package dev.kkorolyov.pancake.input.control

import dev.kkorolyov.pancake.input.event.InputEvent
import dev.kkorolyov.pancake.input.event.MouseButtonEvent
import dev.kkorolyov.pancake.input.event.StateEvent
import dev.kkorolyov.pancake.platform.action.Action

/**
 * Returns [action] for [MouseButtonEvent]s matching [button] and [state].
 */
data class MouseButtonControl(var button: Int, var state: StateEvent.State, var action: Action) : Control {
	override fun invoke(event: InputEvent): Action? = when (event) {
		is MouseButtonEvent -> if (event.button == button && event.state == state) action else null
		else -> null
	}
}
