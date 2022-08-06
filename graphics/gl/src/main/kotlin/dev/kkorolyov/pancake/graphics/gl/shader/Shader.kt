package dev.kkorolyov.pancake.graphics.gl.shader

import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack
import java.io.InputStream

/**
 * Represents an `OpenGL` shader that can be used in a [Program].
 * The backing `OpenGL` object is allocated lazily and cached until [close].
 * A single instance can be reused across shared `OpenGL` contexts.
 */
class Shader(
	/**
	 * Shader type.
	 */
	type: Type,
	/**
	 * Shader source strings.
	 */
	vararg sources: String
) : AutoCloseable {
	constructor(
		/**
		 * Shader type.
		 */
		type: Type,
		/**
		 * Shader sources.
		 */
		vararg sources: InputStream
	) : this(
		type,
		*sources
			.map(InputStream::readAllBytes)
			.map(::String)
			.toTypedArray()
	)

	private val id = Cache {
		val id = glCreateShader(type.value)
		if (id == 0) throw IllegalStateException("Cannot create shader")

		glShaderSource(id, *sources)
		glCompileShader(id)

		MemoryStack.stackPush().use {
			val statusP = it.mallocInt(1)
			glGetShaderiv(id, GL_COMPILE_STATUS, statusP)

			if (statusP[0] == GL_FALSE) throw IllegalArgumentException("Cannot compile $type shader: ${glGetShaderInfoLog(id)}")
		}
		id
	}

	/**
	 * Attaches this shader to [program].
	 */
	fun attach(program: Int) {
		glAttachShader(program, id())
	}

	/**
	 * Detaches this shader from [program].
	 */
	fun detach(program: Int) {
		glDetachShader(program, id())
	}

	/**
	 * Deletes the backing `OpenGL` object if it has been initialized.
	 * Subsequent interactions with this shader will first initialize a new backing object.
	 */
	override fun close() {
		if (id.initialized) {
			glDeleteShader(id())
			id.invalidate()
		}
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
}
