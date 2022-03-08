package dev.kkorolyov.pancake.input.glfw.input

/**
 * Indicates a user input.
 */
interface InputEvent {
	/**
	 * ID of window emitting event.
	 */
	val window: Long
}
