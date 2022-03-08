package dev.kkorolyov.pancake.audio.al.component

import dev.kkorolyov.pancake.audio.al.AudioSource
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3

/**
 * Emits positional audio.
 */
class AudioEmitter(
	/** Source to emit. */
	private val source: AudioSource
) : Component {
	/**
	 * Sets the position of the emitted source to [position].
	 */
	fun setPosition(position: Vector3) {
		source.position = position
	}

	/**
	 * Sets the velocity of the emitted source to [velocity].
	 */
	fun setVelocity(velocity: Vector3) {
		source.velocity = velocity
	}

	/**
	 * Plays source if it has not yet played.
	 * Returns `true` if the source is done playing.
	 */
	operator fun invoke(): Boolean {
		if (source.state == AudioSource.State.INITIAL) source.play()
		return source.state == AudioSource.State.STOPPED
	}
}
