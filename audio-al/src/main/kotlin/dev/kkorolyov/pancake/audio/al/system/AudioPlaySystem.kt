package dev.kkorolyov.pancake.audio.al.system

import dev.kkorolyov.pancake.audio.al.component.AudioEmitter
import dev.kkorolyov.pancake.audio.al.internal.alLoad
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Plays entity [AudioEmitter]s that are pending and removes those that are done.
 */
class AudioPlaySystem : GameSystem(AudioEmitter::class.java) {
	override fun before() {
		alLoad()
	}

	override fun update(entity: Entity, dt: Long) {
		// TODO augment frequency with dt
		if (entity[AudioEmitter::class.java]()) entity.remove(AudioEmitter::class.java)
	}
}
