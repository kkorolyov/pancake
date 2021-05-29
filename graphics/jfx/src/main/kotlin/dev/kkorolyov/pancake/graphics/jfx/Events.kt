package dev.kkorolyov.pancake.graphics.jfx

import dev.kkorolyov.pancake.platform.event.Event

/**
 * Adds a camera to the scene.
 */
class AddCamera(val camera: Camera) : Event

/**
 * Removes a camera from the scene.
 */
class RemoveCamera(val camera: Camera) : Event
