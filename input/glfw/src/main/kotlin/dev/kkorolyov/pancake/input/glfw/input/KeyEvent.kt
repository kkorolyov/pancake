package dev.kkorolyov.pancake.input.glfw.input

/**
 * Indicates a state change in an individual key.
 */
data class KeyEvent(
	override val window: Long,
	/** `GLFW` key code. */
	val key: Int,
	/** `GLFW` scan code. */
	val scanCode: Int,
	/** Key action. */
	override val state: StateEvent.State,
	/** `GLFW` key modifier bits. */
	val mods: Int
) : StateEvent
