package dev.kkorolyov.pancake.graphics.common.system

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.graphics.common.Camera
import dev.kkorolyov.pancake.graphics.common.CameraCreated
import dev.kkorolyov.pancake.graphics.common.CameraDestroyed
import dev.kkorolyov.pancake.graphics.common.component.Lens
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.utility.Limiter

/**
 * Monitors and broadcasts camera creation and destruction events.
 */
class CameraSystem : GameSystem(
	listOf(Lens::class.java, Transform::class.java),
	Limiter.fromConfig(CameraSystem::class.java)
) {
	private val known: MutableSet<Int> = mutableSetOf()
	private val seen: MutableSet<Int> = mutableSetOf()

	override fun before(dt: Long) {
		seen.clear()
		seen.addAll(known)
	}

	override fun update(entity: Entity, dt: Long) {
		if (known.add(entity.id)) enqueue(CameraCreated(Camera(entity)))
		seen.remove(entity.id)
	}

	override fun after(dt: Long) {
		seen.forEach {
			enqueue(CameraDestroyed(it))
			known.remove(it)
		}
	}
}
