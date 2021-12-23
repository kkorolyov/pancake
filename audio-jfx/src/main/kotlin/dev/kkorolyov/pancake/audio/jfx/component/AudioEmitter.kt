package dev.kkorolyov.pancake.audio.jfx.component

import dev.kkorolyov.pancake.platform.entity.Component
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.util.concurrent.ConcurrentHashMap

/**
 * Maintains audio state at a given configuration.
 */
class AudioEmitter : Component {
	/**
	 * Controls the playing state of all contained audio media.
	 * State changes take effect at the next [invoke].
	 */
	var active: Boolean = true

	private val queue = mutableListOf<MediaPlayer>()
	private val players = ConcurrentHashMap.newKeySet<MediaPlayer>()

	/**
	 * Adds [media] to this emitter's queue to be started at the next [invoke].
	 */
	fun add(media: Media) {
		queue.add(MediaPlayer(media))
	}

	/**
	 * Activates all queued media and updates all media to play at [volume] (constrained `[0.0, 1.0]`) and [balance] (constrained `[-1.0, 1.0]`).
	 * Sets the playing state of all media according to [active].
	 */
	operator fun invoke(volume: Double, balance: Double) {
		queue.forEach {
			players.add(it)
			it.onEndOfMedia = Runnable { players.remove(it) }
		}
		queue.clear()

		players.forEach {
			it.volume = volume
			it.balance = balance

			if (active) it.play() else it.pause()
		}
	}
}
