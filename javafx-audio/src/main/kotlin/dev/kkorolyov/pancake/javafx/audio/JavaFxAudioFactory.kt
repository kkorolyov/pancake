package dev.kkorolyov.pancake.javafx.audio

import dev.kkorolyov.pancake.platform.media.audio.Audio
import dev.kkorolyov.pancake.platform.media.audio.AudioFactory
import dev.kkorolyov.simplefuncs.function.Memoizer.memoize
import javafx.scene.media.Media

/**
 * [AudioFactory] implemented through JavaFX.
 */
class JavaFxAudioFactory : AudioFactory {
	private val mediaCache: (String) -> Media = memoize<String, Media>(::Media)::apply

	override fun get(uri: String): Audio = JavaFxAudio(mediaCache(uri))
}
