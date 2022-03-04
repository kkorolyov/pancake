package dev.kkorolyov.pancake.input.glfw.input

/**
 * Indicates a cursor position change.
 */
data class CursorPosEvent(
	override val window: Long,
	/** `x` screen coordinate relative to left edge */
	val x: Double,
	/** `y` screen coordinate relative to top edge */
	val y: Double
) : InputEvent()
