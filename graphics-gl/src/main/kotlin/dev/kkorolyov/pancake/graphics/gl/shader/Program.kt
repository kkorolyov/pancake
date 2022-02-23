package dev.kkorolyov.pancake.graphics.gl.shader

import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack

/**
 * Represents an `OpenGL` program that can be [use]d for rendering.
 */
class Program(
	/**
	 * ID of the referenced program.
	 */
	val id: Int
) {
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
				uP.put(xx.toFloat())
				uP.put(yx.toFloat())
				uP.put(zx.toFloat())
				uP.put(wx.toFloat())

				uP.put(xy.toFloat())
				uP.put(yy.toFloat())
				uP.put(zy.toFloat())
				uP.put(wy.toFloat())

				uP.put(xz.toFloat())
				uP.put(yz.toFloat())
				uP.put(zz.toFloat())
				uP.put(wz.toFloat())

				uP.put(xw.toFloat())
				uP.put(yw.toFloat())
				uP.put(zw.toFloat())
				uP.put(ww.toFloat())
			}

			uP.position(0)

			glUniformMatrix4fv(glGetUniformLocation(id, name), false, uP)
		}
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


	companion object {
		/**
		 * Returns a [Program] linking all [shaders].
		 */
		fun link(vararg shaders: Shader): Program {
			val id = glCreateProgram()
			shaders.forEach { glAttachShader(id, it.id) }
			glLinkProgram(id)

			MemoryStack.stackPush().use {
				val statusP = it.callocInt(1)
				glGetProgramiv(id, GL_LINK_STATUS, statusP)

				if (statusP[0] == GL_FALSE) throw IllegalArgumentException("Cannot compile shader program: ${glGetProgramInfoLog(id)}")
			}

			shaders.forEach { glDetachShader(id, it.id) }

			return Program(id)
		}
	}
}
