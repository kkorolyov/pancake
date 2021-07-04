package dev.kkorolyov.pancake.audio.jfx

import dev.kkorolyov.pancake.platform.event.Event

/**
 * Adds an audio [listener].
 */
class AddListener(val listener: Listener) : Event

/**
 * Removes an audio [listener].
 */
class RemoveListener(val listener: Listener) : Event

/**
 * Sets all active audio state to playing if [active], otherwise to paused.
 */
class SetAudioState(val active: Boolean) : Event
