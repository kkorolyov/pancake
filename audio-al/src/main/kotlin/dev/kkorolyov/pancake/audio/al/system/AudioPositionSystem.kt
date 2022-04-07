package dev.kkorolyov.pancake.audio.al.system

import dev.kkorolyov.pancake.audio.al.component.AudioEmitter
import dev.kkorolyov.pancake.audio.al.internal.alLoad
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Positions entity [AudioEmitter]s according to the entity's transform.
 */
class AudioPositionSystem : GameSystem(AudioEmitter::class.java, Transform::class.java) {
	override fun before() {
		alLoad()
	}

	override fun update(entity: Entity, dt: Long) {
		entity[AudioEmitter::class.java].setPosition(entity[Transform::class.java].globalPosition)
	}
}
