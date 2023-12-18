package dev.kkorolyov.pancake.graphics.system

import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.graphics.CameraQueue
import dev.kkorolyov.pancake.graphics.component.Lens
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import java.util.BitSet

/**
 * Adds and removes camera entities from a shared queue.
 */
class CameraSystem(private val queue: CameraQueue) : GameSystem(Lens::class.java, Position::class.java) {
	private val seen = BitSet()

	override fun update(entity: Entity, dt: Long) {
		queue += entity
		seen.set(entity.id, false)
	}

	override fun after(dt: Long) {
		var i = seen.nextSetBit(0)
		while (i > 0) {
			queue -= i
			i = seen.nextSetBit(i)
		}

		seen.clear()

		queue.ids.forEach(seen::set)
	}
}
