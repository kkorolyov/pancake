package dev.kkorolyov.pancake.input.glfw.input

/**
 * Indicates a user input.
 */
abstract class InputEvent {
	/**
	 * ID of window emitting event.
	 */
	abstract val window: Long
}
