package dev.kkorolyov.pancake.input.event

/**
 * Indicates a state change in mouse buttons.
 */
data class MouseButtonEvent(
	val button: Int,
	override val state: StateEvent.State,
	/** Key modifiers as a bitset. */
	val mods: Int
) : StateEvent
