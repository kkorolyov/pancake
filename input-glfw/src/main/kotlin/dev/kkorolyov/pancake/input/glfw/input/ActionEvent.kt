package dev.kkorolyov.pancake.input.glfw.input

import org.lwjgl.glfw.GLFW

/**
 * Indicates a distinct invoked action.
 */
interface ActionEvent : InputEvent {
	/** Invoked action. */
	val action: Action

	/**
	 * State change action.
	 */
	enum class Action(
		/** Represented `GLFW` value. */
		val value: Int
	) {
		/** Was released. */
		RELEASE(GLFW.GLFW_RELEASE),

		/** Was pressed. */
		PRESS(GLFW.GLFW_PRESS),

		/** Was held until repeat. */
		REPEAT(GLFW.GLFW_REPEAT);

		companion object {
			/**
			 * Returns the action represented by the `GLFW` [value].
			 */
			fun forValue(value: Int): Action = values()[value]
		}
	}
}
