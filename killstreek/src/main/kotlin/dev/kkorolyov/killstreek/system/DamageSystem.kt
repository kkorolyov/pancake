package dev.kkorolyov.killstreek.system

import dev.kkorolyov.killstreek.component.Damage
import dev.kkorolyov.killstreek.component.Health
import dev.kkorolyov.pancake.platform.plugin.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.utility.Limiter

/**
 * Applies damage to entity health.
 */
class DamageSystem : GameSystem(
		Signature(Health::class.java, Damage::class.java),
		Limiter.fromConfig(DamageSystem::class.java)
) {
	override fun update(entity: Entity, dt: Long) {
		entity.get(Damage::class.java).apply(entity.get(Health::class.java), dt)
	}
}
