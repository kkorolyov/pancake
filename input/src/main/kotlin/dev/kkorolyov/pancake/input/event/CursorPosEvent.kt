package dev.kkorolyov.pancake.input.event

/**
 * Indicates a cursor position change.
 */
data class CursorPosEvent(
	/** screen coordinate from left edge */
	val x: Double,
	/** screen coordinate from top edge */
	val y: Double
) : InputEvent
