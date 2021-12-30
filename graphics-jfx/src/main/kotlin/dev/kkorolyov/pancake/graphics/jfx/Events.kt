package dev.kkorolyov.pancake.graphics.jfx

import dev.kkorolyov.pancake.platform.event.Event

/**
 * Notes that a [camera] entity has been created.
 */
class CameraCreated(
	/**
	 * Created camera.
	 */
	val camera: Camera
) : Event

/**
 * Notes that the camera entity with [id] has been destroyed.
 */
class CameraDestroyed(
	/**
	 * ID of destroyed camera.
	 */
	val id: Int
) : Event
