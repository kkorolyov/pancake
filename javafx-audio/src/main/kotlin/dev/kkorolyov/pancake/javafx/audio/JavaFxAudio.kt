package dev.kkorolyov.pancake.javafx.audio

import dev.kkorolyov.pancake.platform.media.audio.Audio
import dev.kkorolyov.pancake.platform.media.audio.Audio.State
import dev.kkorolyov.pancake.platform.media.audio.Audio.State.END
import dev.kkorolyov.pancake.platform.media.audio.Audio.State.PAUSE
import dev.kkorolyov.pancake.platform.media.audio.Audio.State.PLAY
import dev.kkorolyov.pancake.platform.media.audio.Audio.State.STOP
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

/**
 * [Audio] implemented through JavaFx.
 */
class JavaFxAudio(media: Media) : Audio {
	private val player: MediaPlayer = MediaPlayer(media)

	override fun on(state: State, handler: Runnable) {
		player.run {
			when (state) {
				PLAY -> onPlaying = handler
				PAUSE -> onPaused = handler
				STOP -> onStopped = handler
				END -> onEndOfMedia = handler
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
