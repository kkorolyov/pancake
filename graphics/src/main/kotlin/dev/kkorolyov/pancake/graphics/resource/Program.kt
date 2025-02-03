package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3

/**
 * A usable program composed of shaders and uniforms to render with.
 */
class Program(vararg shaders: Shader) : RenderResource() {
	private val _shaders = mutableListOf<Shader>().apply { addAll(shaders) }
	var shaders: Iterable<Shader>
		get() = _shaders
		set(value) {
			_shaders.clear()
			_shaders.addAll(value)
			invalidate()
		}
	val uniforms: Uniforms = Uniforms()

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Program) return false

		if (shaders != other.shaders) return false
		if (uniforms != other.uniforms) return false

		return true
	}

	override fun hashCode(): Int {
		var result = shaders.hashCode()
		result = 31 * result + uniforms.hashCode()
		return result
	}

	class Uniforms : RenderResource(), Iterable<Map.Entry<Int, Any>> {
		private val data = mutableMapOf<Int, Any>()

		/**
		 * The number of entries.
		 */
		val size: Int by data::size

		/**
		 * Sets the [location] uniform's [value].
		 */
		operator fun set(location: Int, value: Float) {
			setRaw(location, value)
		}
		/**
		 * Sets the [location] uniform's [value].
		 */
		operator fun set(location: Int, value: Vector2) {
			setRaw(location, value)
		}
		/**
		 * Sets the [location] uniform's [value].
		 */
		operator fun set(location: Int, value: Vector3) {
			setRaw(location, value)
		}
		/**
		 * Sets the [location] uniform's [value].
		 */
		operator fun set(location: Int, value: Matrix4) {
			setRaw(location, value)
		}

		/**
		 * Sets the [location] uniform's [value].
		 * Prefer the typesafe [set] methods over this when possible.
		 */
		fun setRaw(location: Int, value: Any) {
			data[location] = value
			invalidate()
		}

		override fun iterator(): Iterator<Map.Entry<Int, Any>> = data.iterator()

		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is Uniforms) return false

			if (data != other.data) return false

			return true
		}

		override fun hashCode(): Int {
			return data.hashCode()
		}
	}
}
