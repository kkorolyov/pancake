package dev.kkorolyov.pancake.audio.al.component

import dev.kkorolyov.pancake.audio.al.AudioListener
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3

/**
 * Tags an entity as receiving positional audio.
 */
class AudioReceiver : Component {
	/**
	 * Listener position.
	 */
	var position: Vector3 by AudioListener::position

	/**
	 * Listener velocity.
	 */
	var velocity: Vector3 by AudioListener::velocity
}
