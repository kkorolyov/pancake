package dev.kkorolyov.pancake.graphics

import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Mutable holder of current cameras.
 */
class CameraQueue {
	private val values = mutableMapOf<Int, Camera>()

	/**
	 * If `entity` is not already present in this queue, adds it and assigns a [Camera] to it.
	 */
	operator fun plusAssign(entity: Entity) {
		values.getOrPut(entity.id) { Camera(entity) }
	}

	/**
	 * Removes any entity matching `id` and its associated [Camera] from this queue.
	 */
	operator fun minusAssign(id: Int) {
		values.remove(id)
	}

	/**
	 * Removes all cameras from this queue.
	 */
	fun clear() {
		values.clear()
	}

	/**
	 * IDs of all camera entities in this queue.
	 */
	val ids: Collection<Int>
		get() = values.keys

	/**
	 * All cameras in this queue.
	 */
	val cameras: Collection<Camera>
		get() = values.values
}
