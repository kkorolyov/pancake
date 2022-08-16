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
	vararg shaders: Shader,
	uniforms: Map<Int, Any> = mapOf()
) : Program {
	constructor(vararg shaders: Shader, init: Setup.() -> Unit) : this(*shaders, uniforms = Setup().apply(init).uniforms)

	private val uniforms = uniforms.toMap()

	private val cache = Cache {
		val id = glCreateProgram()
		if (id == 0) throw IllegalStateException("Cannot create shader program")

		shaders.forEach { glAttachShader(id, it.id) }
		glLinkProgram(id)
		shaders.forEach { glDetachShader(id, it.id) }

		if (glGetProgrami(id, GL_LINK_STATUS) == 0) throw IllegalArgumentException("Cannot link shader program: ${glGetProgramInfoLog(id)}")

		this.uniforms.forEach { (location, value) ->
			when (value) {
				is Float -> set(id, location, value)
				is Vector3 -> set(id, location, value)
				is Vector2 -> set(id, location, value)
				is Matrix4 -> set(id, location, value)
				else -> throw IllegalArgumentException("unknown uniform type: ${value::class}")
			}
		}

		id
	}

	override fun activate() {
		glUseProgram(id)
	}

	override fun deactivate() {
		glUseProgram(0)
	}

	override fun set(location: Int, value: Float) {
		set(id, location, value)
	}

	private fun set(id: Int, location: Int, value: Float) {
		glProgramUniform1f(id, location, value)
	}

	override fun set(location: Int, value: Vector2) {
		set(id, location, value)
	}

	private fun set(id: Int, location: Int, value: Vector2) {
		glProgramUniform2f(id, location, value.x.toFloat(), value.y.toFloat())
	}

	override fun set(location: Int, value: Vector3) {
		set(id, location, value)
	}

	private fun set(id: Int, location: Int, value: Vector3) {
		glProgramUniform3f(id, location, value.x.toFloat(), value.y.toFloat(), value.z.toFloat())
	}

	override fun set(location: Int, value: Matrix4) {
		set(id, location, value)
	}

	private fun set(id: Int, location: Int, value: Matrix4) {
		MemoryStack.stackPush().use { stack ->
			glProgramUniformMatrix4fv(id, location, false,
				value.run {
					stack.floats(
						xx.toFloat(),
						yx.toFloat(),
						zx.toFloat(),
						wx.toFloat(),

						xy.toFloat(),
						yy.toFloat(),
						zy.toFloat(),
						wy.toFloat(),

						xz.toFloat(),
						yz.toFloat(),
						zz.toFloat(),
						wz.toFloat(),

						xw.toFloat(),
						yw.toFloat(),
						zw.toFloat(),
						ww.toFloat()
					)
				}
			)
		}
	}

	override val id by cache

	override fun close() {
		cache.invalidate(GL46::glDeleteProgram)
	}

	/**
	 * Configures program uniform values.
	 */
	class Setup {
		internal val uniforms = mutableMapOf<Int, Any>()

		/**
		 * Sets the [location] uniform's initial [value].
		 */
		fun set(location: Int, value: Float) {
			uniforms[location] = value
		}
		/**
		 * Sets the [location] uniform's initial [value].
		 */
		fun set(location: Int, value: Vector2) {
			uniforms[location] = value
		}
		/**
		 * Sets the [location] uniform's initial [value].
		 */
		fun set(location: Int, value: Vector3) {
			uniforms[location] = value
		}
		/**
		 * Sets the [location] uniform's initial [value].
		 */
		fun set(location: Int, value: Matrix4) {
			uniforms[location] = value
		}
	}
}
