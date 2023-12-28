package dev.kkorolyov.pancake.graphics.gl.system

import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.graphics.Camera
import dev.kkorolyov.pancake.graphics.CameraQueue
import dev.kkorolyov.pancake.graphics.component.Model
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.graphics.scoped
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
) : GameSystem(Position::class.java, Model::class.java) {
	private val pending: MutableMap<Program, MutableList<Entity>> = mutableMapOf()
	private val clipTransform: Matrix4 = Matrix4.identity()

	override fun update(entity: Entity, dt: Long) {
		pending.getOrPut(entity[Model::class.java].program) { mutableListOf() }.add((entity))
	}

	override fun after(dt: Long) {
		queue.cameras.forEach { camera ->
			camera.lens.apply {
				glViewport(offset.x.toInt(), offset.y.toInt(), size.x.toInt(), size.y.toInt())
			}

			pending.forEach { (program, entities) ->
				program.scoped {
					entities.forEach {
						val meshes = it[Model::class.java].meshes
						val position = it[Position::class.java].globalValue
						val cameraPosition = camera.position.globalValue

						camera.lens.let {
							clipTransform.xx = it.scale.x / it.size.x * 2
							clipTransform.yy = it.scale.y / it.size.y * 2
						}

						clipTransform.xw = (position.x - cameraPosition.x) * clipTransform.xx
						clipTransform.yw = (position.y - cameraPosition.y) * clipTransform.yy
						clipTransform.zw = position.z - cameraPosition.z

						program[0] = clipTransform

						meshes.forEach(Mesh::draw)
					}
				}

				// clear for next update
				entities.clear()
			}
		}
	}
}
