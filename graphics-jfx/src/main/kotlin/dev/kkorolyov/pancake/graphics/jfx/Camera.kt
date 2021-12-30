package dev.kkorolyov.pancake.graphics.jfx

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.graphics.jfx.component.Lens
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vectors
import java.lang.IllegalStateException

/**
 * Wraps an entity acting as a camera viewing some scene.
 */
class Camera(
	private val entity: Entity
) {
	/**
	 * Unique camera ID.
	 */
	val id: Int
		get() = entity.id

	/**
	 * Camera transform.
	 */
	val transform: Transform
		get() = entity.get(Transform::class.java) ?: throw IllegalStateException("camera must have a transform")

	/**
	 * Camera lens.
	 */
	val lens: Lens
		get() = entity.get(Lens::class.java) ?: throw IllegalStateException("camera must have a lens")

	private val computation: Vector2 = Vectors.create(0.0, 0.0)

	/**
	 * Returns the result of mapping the given absolute [position] vector to pixel coordinates relative to this camera's view.
	 */
	fun map(position: Vector2): Vector2 = computation.apply {
		set(position)
		add(transform.globalPosition, -1.0)

		val scale = lens.scale
		x *= scale.x
		// Negate as pixel rows increment downward
		y *= -scale.y
	}
}
