package dev.kkorolyov.pancake.graphics.gl.shader

import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack
import java.io.InputStream

/**
 * Represents an `OpenGL` shader that can be used in a [Program].
 */
class Shader(
	/**
	 * ID of the referenced shader.
	 */
	val id: Int
) : AutoCloseable {
	/**
	 * Deletes this shader.
	 */
	override fun close() {
		glDeleteShader(id)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Shader

		return id == other.id
	}

	override fun hashCode(): Int {
		return id
	}


	/**
	 * Shader type.
	 */
	enum class Type(
		/**
		 * `OpenGL` value represented by this type.
		 */
		val value: Int
	) {
		VERTEX(GL_VERTEX_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER)
	}

	companion object {
		/**
		 * Returns a [type] [Shader] compiled from concatenated strings read from [sources].
		 */
		fun compile(type: Type, vararg sources: InputStream): Shader = compile(
			type,
			*sources
				.map { it.readAllBytes() }
				.map { String(it) }
				.toTypedArray()
		)

		/**
		 * Returns a [type] [Shader] compiled from concatenated [sources].
		 */
		fun compile(type: Type, vararg sources: String): Shader {
			val id = glCreateShader(type.value)
			glShaderSource(id, *sources)
			glCompileShader(id)

			MemoryStack.stackPush().use {
				val statusP = it.callocInt(1)
				glGetShaderiv(id, GL_COMPILE_STATUS, statusP)

				if (statusP[0] == GL_FALSE) throw IllegalArgumentException("Cannot compile $type shader: ${glGetShaderInfoLog(id)}")
			}

			return Shader(id)
		}
	}
}
