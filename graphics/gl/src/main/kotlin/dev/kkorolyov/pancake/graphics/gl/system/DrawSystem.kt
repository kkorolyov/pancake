package dev.kkorolyov.pancake.graphics.gl.system

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.graphics.Camera
import dev.kkorolyov.pancake.graphics.CameraQueue
import dev.kkorolyov.pancake.graphics.gl.component.Model
import dev.kkorolyov.pancake.graphics.gl.mesh.Mesh
import dev.kkorolyov.pancake.graphics.gl.shader.Program
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Matrix4
import org.lwjgl.opengl.GL46.*

/**
 * Draws entity [Model]s from the perspectives of all active [Camera]s.
 * Each program is provided a `(location = 0) mat4` uniform transforming vertices to clip space.
 */
class DrawSystem(
	private val queue: CameraQueue
) : GameSystem(Transform::class.java, Model::class.java) {
	private val pending: MutableMap<Program, MutableList<Entity>> = mutableMapOf()
	private val transform: Matrix4 = Matrix4.identity()

	override fun update(entity: Entity, dt: Long) {
		pending.getOrPut(entity[Model::class.java].program) { mutableListOf() }.add((entity))
	}

	override fun after() {
		queue.cameras.forEach { camera ->
			camera.lens.apply {
				glViewport(offset.x.toInt(), offset.y.toInt(), size.x.toInt(), size.y.toInt())
			}

			pending.forEach { (program, entities) ->
				program()
				entities.forEach {
					val meshes = it[Model::class.java].meshes
					val position = it[Transform::class.java].globalPosition

					camera.lens.let {
						transform.xx = it.scale.x / it.size.x * 2
						transform.yy = it.scale.y / it.size.y * 2
					}
					transform.xw = (position.x - camera.transform.globalPosition.x) * transform.xx
					transform.yw = (position.y - camera.transform.globalPosition.y) * transform.yy
					transform.zw = position.z - camera.transform.globalPosition.z

					program[0] = transform

					meshes.forEach(Mesh::draw)
				}
				entities.clear()
			}
		}

		pending.clear()
	}
}
