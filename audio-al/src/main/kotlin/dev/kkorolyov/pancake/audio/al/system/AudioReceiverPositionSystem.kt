package dev.kkorolyov.pancake.audio.al.system

import dev.kkorolyov.pancake.audio.al.component.AudioReceiver
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Positions entity [AudioReceiver]s according to the entity's transform.
 */
class AudioReceiverPositionSystem : GameSystem(AudioReceiver::class.java, Transform::class.java) {
	override fun update(entity: Entity, dt: Long) {
		entity[AudioReceiver::class.java].position = entity[Transform::class.java].globalPosition
	}
}
