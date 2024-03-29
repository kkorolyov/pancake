package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.graphics.GraphicsResource
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3

/**
 * A usable program composed of shaders and uniforms to render with.
 */
interface Program : GraphicsResource.Active {
	/**
	 * Sets the [location] uniform's [value].
	 */
	operator fun set(location: Int, value: Float)

	/**
	 * Sets the [location] uniform's [value].
	 */
	operator fun set(location: Int, value: Vector2)

	/**
	 * Sets the [location] uniform's [value].
	 */
	operator fun set(location: Int, value: Vector3)

	/**
	 * Sets the [location] uniform's [value].
	 */
	operator fun set(location: Int, value: Matrix4)
}
