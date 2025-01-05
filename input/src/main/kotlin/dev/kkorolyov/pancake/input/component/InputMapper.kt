package dev.kkorolyov.pancake.input.component

import dev.kkorolyov.pancake.input.control.Control
import dev.kkorolyov.pancake.input.event.InputEvent
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Component

/**
 * Returns actions in response to input events.
 * Configured as a list of [Control]s.
 */
class InputMapper : Component, Iterable<Control> {
	private val controls = mutableListOf<Control>()

	/**
	 * The number of contained controls.
	 */
	val size: Int
		get() = controls.size

	/**
	 * Adds [control].
	 */
	operator fun plusAssign(control: Control) {
		controls += control
	}
	/**
	 * Removes [control].
	 */
	operator fun minusAssign(control: Control) {
		controls -= control
	}

	/**
	 * Returns the action of the first control matching [event], if any.
	 */
	operator fun invoke(event: InputEvent): Action? = controls.firstNotNullOfOrNull { it(event) }

	override fun iterator(): Iterator<Control> = controls.iterator()

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is InputMapper) return false

		if (controls != other.controls) return false

		return true
	}

	override fun hashCode(): Int {
		return controls.hashCode()
	}
}
