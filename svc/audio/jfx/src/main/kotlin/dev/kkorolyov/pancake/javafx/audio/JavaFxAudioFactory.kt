package dev.kkorolyov.pancake.javafx.audio

import dev.kkorolyov.flopple.function.Memoizer.memoize
import dev.kkorolyov.pancake.platform.media.audio.Audio
import dev.kkorolyov.pancake.platform.service.AudioFactory
import javafx.scene.media.Media
import java.nio.file.Path

/**
 * [AudioFactory] implemented through JavaFX.
 */
class JavaFxAudioFactory : AudioFactory {
	private val mediaCache: (String) -> Media = memoize<String, Media> { Media(Path.of(it).toUri().toString()) }::apply

	override fun get(uri: String): Audio = JavaFxAudio(mediaCache(uri))
}
