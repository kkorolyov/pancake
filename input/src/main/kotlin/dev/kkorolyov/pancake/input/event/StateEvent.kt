package dev.kkorolyov.pancake.input.event

/**
 * A transition to a different state.
 */
interface StateEvent : InputEvent {
	val state: State

	enum class State {
		RELEASE,
		PRESS,
		REPEAT;
	}
}
