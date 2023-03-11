package dev.kkorolyov.pancake.audio.al.system

import dev.kkorolyov.pancake.audio.al.component.AudioEmitter
import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Positions entity [AudioEmitter]s according to the entity's position.
 */
class AudioPositionSystem : GameSystem(AudioEmitter::class.java, Position::class.java) {
	override fun update(entity: Entity, dt: Long) {
		entity[AudioEmitter::class.java].setPosition(entity[Position::class.java].globalValue)
	}
}
