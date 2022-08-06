package dev.kkorolyov.pancake.graphics.gl

import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import org.lwjgl.opengl.GL46.*
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

		if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) throw IllegalArgumentException("Cannot compile $type shader: ${glGetShaderInfoLog(id)}")

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
		id.invalidate(::glDeleteShader)
	}

	/**
	 * Shader type.
	 */
	enum class Type(
		internal val value: Int
	) {
		VERTEX(GL_VERTEX_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER)
	}
}
