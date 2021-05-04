package dev.kkorolyov.pancake.javafx.audio

import dev.kkorolyov.pancake.platform.media.audio.Audio
import dev.kkorolyov.pancake.platform.media.audio.Audio.State
import dev.kkorolyov.pancake.platform.media.audio.Audio.State.*
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

/**
 * [Audio] implemented through JavaFx.
 */
class JavaFxAudio(media: Media) : Audio {
	private val player: MediaPlayer = MediaPlayer(media).apply {
		// Default behavior remains playing state
		onEndOfMedia = Runnable { stop() }
	}

	override fun on(state: State, handler: Runnable) {
		player.run {
			when (state) {
				PLAY -> onPlaying = handler
				PAUSE -> onPaused = handler
				STOP -> onStopped = handler
				// Retain default stop functionality
				END -> onEndOfMedia = Runnable {
					stop()
					handler.run()
				}
			}
		}
	}

	override fun state(state: State) {
		player.run {
			when (state) {
				PLAY -> play()
				PAUSE -> pause()
				STOP -> stop()
				END -> dispose()
			}
		}
	}

	override fun setVolume(volume: Double) {
		player.volume = volume
	}

	override fun setBalance(balance: Double) {
		player.balance = balance
	}
}
