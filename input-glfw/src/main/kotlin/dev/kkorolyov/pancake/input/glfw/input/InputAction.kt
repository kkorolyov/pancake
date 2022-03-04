package dev.kkorolyov.pancake.input.glfw.input

import org.lwjgl.glfw.GLFW

/**
 * State change action.
 */
enum class InputAction(
	/** Represented `GLFW` value. */
	val value: Int
) {
	/** Was pressed. */
	PRESS(GLFW.GLFW_PRESS),

	/** Was released. */
	RELEASE(GLFW.GLFW_RELEASE),

	/** Was held until repeat. */
	REPEAT(GLFW.GLFW_REPEAT);

	companion object {
		/**
		 * Returns the action represented by the `GLFW` [value].
		 */
		fun forValue(value: Int): InputAction = values().first { it.value == value }
	}
}
