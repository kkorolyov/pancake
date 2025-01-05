package dev.kkorolyov.pancake.input.event

/**
 * Indicates a state change in an individual key.
 */
data class KeyEvent(
	val key: Int,
	val scanCode: Int,
	override val state: StateEvent.State,
	/** Key modifiers as a bitset. */
	val mods: Int
) : StateEvent
