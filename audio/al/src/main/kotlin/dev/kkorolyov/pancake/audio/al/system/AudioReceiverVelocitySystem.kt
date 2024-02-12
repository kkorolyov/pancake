package dev.kkorolyov.pancake.audio.al.system

import dev.kkorolyov.pancake.audio.al.component.AudioReceiver
import dev.kkorolyov.pancake.core.component.Velocity
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Set entity [AudioReceiver] velocities according to the entity's velocity.
 */
class AudioReceiverVelocitySystem : GameSystem(AudioReceiver::class.java, Velocity::class.java) {
	override fun update(entity: Entity, dt: Long) {
		entity[AudioReceiver::class.java].velocity = entity[Velocity::class.java].linear
	}
}
