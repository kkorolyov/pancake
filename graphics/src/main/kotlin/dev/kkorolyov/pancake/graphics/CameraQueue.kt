package dev.kkorolyov.pancake.graphics

import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Mutable holder of current cameras.
 */
class CameraQueue {
	private val values = mutableMapOf<Entity, Camera>()

	/**
	 * If `entity` is not already present in this queue, adds it and assigns a [Camera] to it.
	 */
	operator fun plusAssign(entity: Entity) {
		values.getOrPut(entity) { Camera(entity) }
	}

	/**
	 * Removes `entity` and its associated [Camera] from this queue, if any.
	 */
	operator fun minusAssign(entity: Entity) {
		values.remove(entity)
	}

	/**
	 * Removes all cameras from this queue.
	 */
	fun clear() {
		values.clear()
	}

	/**
	 * All cameras in this queue.
	 */
	val cameras: Collection<Camera>
		get() = values.values
}
