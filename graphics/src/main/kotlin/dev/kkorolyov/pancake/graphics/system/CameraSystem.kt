package dev.kkorolyov.pancake.graphics.system

import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.graphics.CameraQueue
import dev.kkorolyov.pancake.graphics.component.Lens
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Adds and removes camera entities from a shared queue.
 */
class CameraSystem(private val queue: CameraQueue) : GameSystem(Lens::class.java, Position::class.java) {
	private val seen: MutableSet<Int> = mutableSetOf()

	override fun update(entity: Entity, dt: Long) {
		queue += entity
		seen.remove(entity.id)
	}

	override fun after(dt: Long) {
		seen.forEach(queue::minusAssign)
		seen.clear()
		seen.addAll(queue.ids)
	}
}
