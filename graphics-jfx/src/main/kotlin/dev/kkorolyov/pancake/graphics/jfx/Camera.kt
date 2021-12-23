package dev.kkorolyov.pancake.graphics.jfx

import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vectors
import dev.kkorolyov.pancake.platform.utility.ArgVerify

/**
 * Maps absolute coordinates to pixel coordinates relative to an origin and scale.
 */
class Camera(
	/** Reference representing the render origin of this view */
	private val origin: Vector2,
	scale: Vector2
) {
	val scale: Vector2 = Vectors.create(
		ArgVerify.greaterThan("scale.x", 0.0, scale.x),
		ArgVerify.greaterThan("scale.y", 0.0, scale.y)
	)

	private val computation: Vector2 = Vectors.create(0.0, 0.0)

	/**
	 * Returns the result pf mapping the given absolute [position] vector to pixel coordinates relative to this camera.
	 */
	fun map(position: Vector2): Vector2 = computation.apply {
		set(position)
		add(origin, -1.0)

		x /= scale.x
		// Negate as pixel rows increment downward
		y /= -scale.y
	}
}
