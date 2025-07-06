package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.platform.io.Resources

/**
 * A step in a shader program.
 */
class Shader(type: Type = Type.VERTEX, source: Source) : RenderResource() {
	var type: Type = type
		set(value) {
			field = value
			invalidate()
		}
	var source: Source = source
		set(value) {
			field = value
			invalidate()
		}

	enum class Type {
		VERTEX,
		FRAGMENT
	}

	/**
	 * Provides a shader source code string.
	 */
	interface Source {
		/**
		 * Shader source code as a string.
		 */
		val value: String

		/**
		 * Source code as a raw string.
		 */
		class Raw(override val value: String) : Source

		/**
		 * Reads source code from the file at [path].
		 */
		class Path(val path: String) : Source {
			override val value: String by lazy { Resources.inStream(path).use { String(it.readAllBytes()) } }
		}
	}
}
