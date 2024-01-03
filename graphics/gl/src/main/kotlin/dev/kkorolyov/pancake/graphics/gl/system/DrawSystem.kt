package dev.kkorolyov.pancake.graphics.gl.system

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.graphics.Camera
import dev.kkorolyov.pancake.graphics.CameraQueue
import dev.kkorolyov.pancake.graphics.component.Model
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.graphics.scoped
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.opengl.GL46.*

/**
 * Draws entity [Model]s from the perspectives of all active [Camera]s.
 * Each program is provided a `(location = 0) mat4` uniform transforming vertices to clip space.
 */
class DrawSystem(
	private val queue: CameraQueue
) : GameSystem(Transform::class.java, Model::class.java) {
	private val pending: MutableMap<Program, MutableList<Entity>> = mutableMapOf()

	// build camera-specific projection only once
	private val clipViewMatrix: Matrix4 = Matrix4.of()
	// full transformation matrix
	private val fullMatrix: Matrix4 = Matrix4.of()
	private val vec3 = Vector3.of()

	override fun update(entity: Entity, dt: Long) {
		pending.getOrPut(entity[Model::class.java].program) { mutableListOf() }.add((entity))
	}

	override fun after(dt: Long) {
		queue.cameras.forEach { camera ->
			clipViewMatrix.reset()

			camera.lens.apply {
				glViewport(offset.x.toInt(), offset.y.toInt(), size.x.toInt(), size.y.toInt())

				// projection matrix
				// *2 because range is [-1, 1], not [0, 1]
				vec3.reset()
				vec3.x = scale.x / size.x * 2
				vec3.y = scale.y / size.y * 2
				vec3.z = 1.0
				clipViewMatrix.scale(vec3)
			}

			// and add view matrix
			clipViewMatrix.multiply(camera.transform.matrix)

			pending.forEach { (program, entities) ->
				program.scoped {
					entities.forEach {
						fullMatrix.set(clipViewMatrix)
						// and add model matrix
						fullMatrix.multiply(it[Transform::class.java].matrix)

						program[0] = fullMatrix

						it[Model::class.java].meshes.forEach(Mesh::draw)
					}
				}

				// clear for next update
				entities.clear()
			}
		}
	}
}
