package dev.kkorolyov.pancake.audio.al.system

import dev.kkorolyov.pancake.audio.al.component.AudioEmitter
import dev.kkorolyov.pancake.core.component.Velocity
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Set entity [AudioEmitter] velocities according to the entity's velocity.
 */
class AudioVelocitySystem : GameSystem(AudioEmitter::class.java, Velocity::class.java) {
	override fun update(entity: Entity, dt: Long) {
		entity[AudioEmitter::class.java].setVelocity(entity[Velocity::class.java].linear)
	}
}
