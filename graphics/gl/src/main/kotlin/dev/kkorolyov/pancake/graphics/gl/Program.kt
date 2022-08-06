package dev.kkorolyov.pancake.graphics.gl

import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack

/**
 * Represents an `OpenGL` program that can be used for rendering.
 * The backing `OpenGL` object is allocated lazily and cached until [close].
 * A single instance can be reused across shared `OpenGL` contexts.
 */
class Program(
	/**
	 * Shaders to link.
	 */
	vararg shaders: Shader
) : AutoCloseable {
	private val id = Cache {
		val id = glCreateProgram()
		if (id == 0) throw IllegalStateException("Cannot create shader program")

		shaders.forEach { it.attach(id) }
		glLinkProgram(id)
		shaders.forEach { it.detach(id) }

		if (glGetProgrami(id, GL_LINK_STATUS) == 0) throw IllegalArgumentException("Cannot link shader program: ${glGetProgramInfoLog(id)}")

		id
	}

	/**
	 * Uses this program as part of the current rendering state.
	 */
	operator fun invoke() {
		glUseProgram(id())
	}

	/**
	 * Sets the [location] uniform's value to [value].
	 */
	operator fun set(location: Int, value: Float) {
		glUniform1f(location, value)
	}

	/**
	 * Sets the [location] uniform's value to [value].
	 */
	operator fun set(location: Int, value: Vector2) {
		glUniform2f(location, value.x.toFloat(), value.y.toFloat())
	}

	/**
	 * Sets the [location] uniform's value to [value].
	 */
	operator fun set(location: Int, value: Vector3) {
		glUniform3f(location, value.x.toFloat(), value.y.toFloat(), value.z.toFloat())
	}

	/**
	 * Sets the [location] uniform's value to [value].
	 */
	operator fun set(location: Int, value: Matrix4) {
		MemoryStack.stackPush().use {
			val uP = it.mallocFloat(16)

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

			glUniformMatrix4fv(location, false, uP)
		}
	}

	/**
	 * Deletes the backing `OpenGL` object if it has been initialized.
	 * Subsequent interactions with this program will first initialize a new backing object.
	 */
	override fun close() {
		id.invalidate(::glDeleteProgram)
	}
}
