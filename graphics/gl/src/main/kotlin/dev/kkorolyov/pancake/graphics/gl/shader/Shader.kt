package dev.kkorolyov.pancake.graphics.gl.shader

import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack
import java.io.InputStream

/**
 * Represents an `OpenGL` shader that can be used in a [Program].
 * The backing `OpenGL` shader is thread-local, so accessing this shader from different threads will affect different `OpenGL` objects.
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

	private val tlId = ThreadLocal.withInitial {
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
	 * ID of this shader in the current thread context.
	 */
	val id: Int
		get() = tlId.get()

	/**
	 * Deletes this shader in the current thread context.
	 * Subsequent access to this shader in the current thread context will recompile it.
	 */
	override fun close() {
		glDeleteShader(id)
		tlId.remove()
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
}
