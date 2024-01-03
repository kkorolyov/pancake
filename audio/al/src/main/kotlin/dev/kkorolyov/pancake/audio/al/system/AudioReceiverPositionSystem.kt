package dev.kkorolyov.pancake.audio.al.system

import dev.kkorolyov.pancake.audio.al.component.AudioReceiver
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Vector3

private val tPos = ThreadLocal.withInitial(Vector3::of)

/**
 * Positions entity [AudioReceiver]s according to the entity's position.
 */
class AudioReceiverPositionSystem : GameSystem(AudioReceiver::class.java, Transform::class.java) {
	override fun update(entity: Entity, dt: Long) {
		entity[AudioReceiver::class.java].position = tPos.get().apply {
			scale(0.0)
			transform(entity[Transform::class.java].matrix)
		}
	}
}
