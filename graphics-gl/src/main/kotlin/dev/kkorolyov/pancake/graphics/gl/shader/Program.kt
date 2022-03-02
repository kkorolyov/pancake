package dev.kkorolyov.pancake.graphics.gl.shader

import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack

/**
 * Represents an `OpenGL` program that can be [use]d for rendering.
 * The backing `OpenGL` program is thread-local, so accessing this program from different threads will affect different `OpenGL` objects.
 */
class Program(
	/**
	 * Shaders to link.
	 */
	vararg shaders: Shader
) : AutoCloseable {
	private val tlId = ThreadLocal.withInitial {
		val id = glCreateProgram()
		if (id == 0) throw IllegalStateException("Cannot create shader program")

		try {
			shaders.forEach { glAttachShader(id, it.id) }
			glLinkProgram(id)

			MemoryStack.stackPush().use {
				val statusP = it.callocInt(1)
				glGetProgramiv(id, GL_LINK_STATUS, statusP)

				if (statusP[0] == GL_FALSE) throw IllegalArgumentException("Cannot link shader program: ${glGetProgramInfoLog(id)}")
			}

			shaders.forEach { glDetachShader(id, it.id) }
		} finally {
			shaders.forEach(Shader::close)
		}

		id
	}

	/**
	 * ID of the referenced `OpenGL` program in the current thread context.
	 */
	val id: Int
		get() = tlId.get()

	/**
	 * Uses this program as part of the current rendering state.
	 */
	fun use(): Unit = glUseProgram(id)

	/**
	 * Sets the [name] uniform's value to [value].
	 */
	fun set(name: String, value: Float) {
		glUniform1f(glGetUniformLocation(id, name), value)
	}

	/**
	 * Sets the [name] uniform's value to [value].
	 */
	fun set(name: String, value: Vector2) {
		glUniform2f(glGetUniformLocation(id, name), value.x.toFloat(), value.y.toFloat())
	}

	/**
	 * Sets the [name] uniform's value to [value].
	 */
	fun set(name: String, value: Vector3) {
		glUniform3f(glGetUniformLocation(id, name), value.x.toFloat(), value.y.toFloat(), value.z.toFloat())
	}

	/**
	 * Sets the [name] uniform's value to [value].
	 */
	fun set(name: String, value: Matrix4) {
		MemoryStack.stackPush().use {
			val uP = it.callocFloat(16)

			value.apply {
				uP
					.put(xx.toFloat())
					.put(yx.toFloat())
					.put(zx.toFloat())
					.put(wx.toFloat())

					.put(xy.toFloat())
					.put(yy.toFloat())
					.put(zy.toFloat())
					.put(wy.toFloat())

					.put(xz.toFloat())
					.put(yz.toFloat())
					.put(zz.toFloat())
					.put(wz.toFloat())

					.put(xw.toFloat())
					.put(yw.toFloat())
					.put(zw.toFloat())
					.put(ww.toFloat())
			}

			uP.flip()

			glUniformMatrix4fv(glGetUniformLocation(id, name), false, uP)
		}
	}

	/**
	 * Deletes this program in the current thread context.
	 * Subsequent access to this program in the current thread context will re-link it.
	 */
	override fun close() {
		glDeleteProgram(id)
		tlId.remove()
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Program

		return id == other.id
	}

	override fun hashCode(): Int {
		return id
	}
}
