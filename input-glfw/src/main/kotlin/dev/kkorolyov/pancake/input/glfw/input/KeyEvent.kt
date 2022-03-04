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
	val action: InputAction,
	/** `GLFW` key modifier bits. */
	val mods: Int
) : InputEvent()
