package dev.kkorolyov.pancake.platform.media.audio;

/**
 * Generates {@link Audio} instances.
 */
public interface AudioFactory {
	/**
	 * @param uri audio source URI
	 * @return instance of audio source at {@code uri}
	 */
	Audio get(String uri);
}
