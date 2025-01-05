package dev.kkorolyov.pancake.input.control

import dev.kkorolyov.pancake.input.event.InputEvent
import dev.kkorolyov.pancake.platform.action.Action

/**
 * An individual event-action binding.
 */
interface Control {
	/**
	 * Possibly returns an action for [event].
	 */
	operator fun invoke(event: InputEvent): Action?
}
