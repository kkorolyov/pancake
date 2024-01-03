package dev.kkorolyov.pancake.graphics

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.graphics.component.Lens
import dev.kkorolyov.pancake.platform.entity.Entity

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
		get() = entity[Transform::class.java] ?: throw IllegalStateException("camera must have a transform")

	/**
	 * Camera lens.
	 */
	val lens: Lens
		get() = entity[Lens::class.java] ?: throw IllegalStateException("camera must have a lens")
}
