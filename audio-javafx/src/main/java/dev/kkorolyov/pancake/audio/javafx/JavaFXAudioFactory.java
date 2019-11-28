package dev.kkorolyov.pancake.audio.javafx;

import dev.kkorolyov.pancake.platform.media.audio.Audio;
import dev.kkorolyov.pancake.platform.media.audio.AudioFactory;

import javafx.scene.media.Media;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link AudioFactory} implemented through JavaFX.
 */
public class JavaFXAudioFactory implements AudioFactory {
	private final Map<String, Media> mediaCache = new ConcurrentHashMap<>();

	@Override
	public Audio get(String uri) {
		return new JavaFXAudio(mediaCache.computeIfAbsent(uri, Media::new));
	}
}
