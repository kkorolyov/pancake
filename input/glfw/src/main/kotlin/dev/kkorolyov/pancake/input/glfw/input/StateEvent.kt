package dev.kkorolyov.pancake.input.glfw.input

import org.lwjgl.glfw.GLFW

/**
 * Indicates a state change of an input.
 */
interface StateEvent : InputEvent {
	/** Current state. */
	val state: State

	/**
	 * Distinct input state.
	 */
	enum class State(
		/** Represented `GLFW` value. */
		val value: Int
	) {
		/** Is now released. */
		RELEASE(GLFW.GLFW_RELEASE),

		/** Is now pressed. */
		PRESS(GLFW.GLFW_PRESS),

		/** Is held until repeat. */
		REPEAT(GLFW.GLFW_REPEAT);

		companion object {
			/**
			 * Returns the state represented by the `GLFW` [value].
			 */
			fun forValue(value: Int): State = entries[value]
		}
	}
}
