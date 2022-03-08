package dev.kkorolyov.pancake.input.glfw.input

/**
 * Indicates a state change in mouse buttons.
 */
data class MouseButtonEvent(
	override val window: Long,
	/** `GLFW` button code. */
	val button: Int,
	/** Button action. */
	override val action: ActionEvent.Action,
	/** `GLFW` key modifier bits. */
	val mods: Int
) : ActionEvent
