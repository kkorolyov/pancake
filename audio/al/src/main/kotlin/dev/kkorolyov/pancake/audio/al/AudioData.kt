package dev.kkorolyov.pancake.audio.al

/**
 * Attaches audio to a source.
 * Whether a given data instance supports attachment to multiple sources is up to the implementation.
 */
interface AudioData {
	/**
	 * Attaches this data to [source].
	 * Subsequent calls to the same [source] are noop.
	 */
	fun attach(source: AudioSource)
}
