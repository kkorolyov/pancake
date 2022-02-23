package dev.kkorolyov.pancake.graphics.gl.system

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.graphics.common.Camera
import dev.kkorolyov.pancake.graphics.common.CameraCreated
import dev.kkorolyov.pancake.graphics.common.CameraDestroyed
import dev.kkorolyov.pancake.graphics.gl.component.Model
import dev.kkorolyov.pancake.graphics.gl.mesh.Mesh
import dev.kkorolyov.pancake.graphics.gl.shader.Program
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.utility.Limiter
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL46.*

/**
 * Draws entity [Model]s from the perspectives of all active [Camera]s.
 * Each program is provided a `uniform mat4 transform` transforming vertices to clip space.
 */
class DrawSystem(
	/**
	 * Buffer swap callback.
	 */
	private val swap: () -> Unit
) : GameSystem(
	listOf(Transform::class.java, Model::class.java),
	Limiter.fromConfig(DrawSystem::class.java)
) {
	private val cameras: MutableList<Camera> = mutableListOf()
	private val pending: MutableMap<Program, MutableList<Entity>> = mutableMapOf()
	private val transform: Matrix4 = Matrix4.identity()

	override fun attach() {
		register(CameraCreated::class.java) { cameras += it.camera }
		register(CameraDestroyed::class.java) { event -> cameras.removeIf { it.id == event.id } }

		GL.createCapabilities()
	}

	override fun before(dt: Long) {
		glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
	}

	override fun update(entity: Entity, dt: Long) {
		pending.computeIfAbsent(entity[Model::class.java].program) { mutableListOf() }.add((entity))
	}

	override fun after(dt: Long) {
		cameras.forEach { camera ->
			camera.lens.apply {
				glViewport(offset.x.toInt(), offset.y.toInt(), size.x.toInt(), size.y.toInt())
			}

			pending.forEach { (program, entities) ->
				program.use()
				entities.forEach {
					val mesh = it[Model::class.java].mesh
					val position = it[Transform::class.java].globalPosition

					camera.lens.apply {
						transform.xx = scale.x / size.x
						transform.yy = scale.y / size.y
					}
					transform.xw = (position.x - camera.transform.globalPosition.x) * transform.xx
					transform.yw = (position.y - camera.transform.globalPosition.y) * transform.yy
					transform.zw = position.z - camera.transform.globalPosition.z

					program.set("transform", transform)

					mesh.forEach(Mesh::draw)
				}
				entities.clear()
			}
		}
		swap()
	}
}
