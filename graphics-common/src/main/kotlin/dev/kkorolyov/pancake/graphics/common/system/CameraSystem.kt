package dev.kkorolyov.pancake.graphics.common.system

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.graphics.common.CameraQueue
import dev.kkorolyov.pancake.graphics.common.component.Lens
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Adds and removes camera entities from a shared queue.
 */
class CameraSystem(private val queue: CameraQueue) : GameSystem(Lens::class.java, Transform::class.java) {
	private val seen: MutableSet<Int> = mutableSetOf()

	override fun update(entity: Entity, dt: Long) {
		queue += entity
		seen.remove(entity.id)
	}

	override fun after() {
		seen.forEach(queue::minusAssign)
		seen.clear()
		seen.addAll(queue.ids)
	}
}
