package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import dev.kkorolyov.pancake.graphics.resource.Shader
import org.lwjgl.opengl.GL46.*
import java.io.InputStream

/**
 * A `OpenGL` shader that can be reused across shared contexts.
 */
class GLShader(type: Type, vararg sources: String) : Shader {
	constructor(type: Type, vararg sources: InputStream) : this(
		type,
		*sources
			.map(InputStream::readAllBytes)
			.map(::String)
			.toTypedArray()
	)

	private val cache = Cache {
		val id = glCreateShader(type.value)
		if (id == 0) throw IllegalStateException("Cannot create shader")

		glShaderSource(id, *sources)
		glCompileShader(id)

		if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) throw IllegalArgumentException("Cannot compile $type shader: ${glGetShaderInfoLog(id)}")

		id
	}

	override val id by cache

	override fun close() {
		cache.invalidate(::glDeleteShader)
	}

	/**
	 * `OpenGL` shader type.
	 */
	enum class Type(internal val value: Int) {
		VERTEX(GL_VERTEX_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER)
	}
}
