package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.graphics.resource.Shader
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.opengl.GL46
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack

/**
 * An `OpenGL` program that can be reused across shared contexts.
 */
class GLProgram(
	/**
	 * Shaders to link.
	 */
	vararg shaders: Shader
) : Program {
	private val cache = Cache {
		val id = glCreateProgram()
		if (id == 0) throw IllegalStateException("Cannot create shader program")

		shaders.forEach { glAttachShader(id, it.id) }
		glLinkProgram(id)
		shaders.forEach { glDetachShader(id, it.id) }

		if (glGetProgrami(id, GL_LINK_STATUS) == 0) throw IllegalArgumentException("Cannot link shader program: ${glGetProgramInfoLog(id)}")

		id
	}

	override fun activate() {
		glUseProgram(id)
	}

	override fun deactivate() {
		glUseProgram(0)
	}

	override fun set(location: Int, value: Float) {
		glUniform1f(location, value)
	}

	override fun set(location: Int, value: Vector2) {
		glUniform2f(location, value.x.toFloat(), value.y.toFloat())
	}

	override fun set(location: Int, value: Vector3) {
		glUniform3f(location, value.x.toFloat(), value.y.toFloat(), value.z.toFloat())
	}

	override fun set(location: Int, value: Matrix4) {
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

	override val id by cache

	override fun close() {
		cache.invalidate(GL46::glDeleteProgram)
	}
}
