package dev.kkorolyov.killstreek.system

import dev.kkorolyov.killstreek.component.Health
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.event.DestroyEntity
import dev.kkorolyov.pancake.platform.utility.Limiter

/**
 * Removes dead entities.
 */
class GcSystem : GameSystem(
		Signature(Health::class.java),
		Limiter.fromConfig(GcSystem::class.java)
) {
	override fun update(entity: Entity, dt: Long) {
		if (entity.get(Health::class.java).isDead) {
			resources.events.enqueue(DestroyEntity(entity.id))
		}
	}
}
