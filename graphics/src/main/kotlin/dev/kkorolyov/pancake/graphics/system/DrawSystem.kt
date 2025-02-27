package dev.kkorolyov.pancake.graphics.system

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.graphics.Camera
import dev.kkorolyov.pancake.graphics.CameraQueue
import dev.kkorolyov.pancake.graphics.component.Model
import dev.kkorolyov.pancake.graphics.renderBackend
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector3

/**
 * Draws entity [Model]s from the perspectives of all active [Camera]s.
 * Each program is provided a `(location = 0) mat4` uniform transforming vertices to clip space.
 */
class DrawSystem(
	private val queue: CameraQueue
) : GameSystem(Transform::class.java, Model::class.java) {
	private val pending: MutableMap<Program, MutableList<Entity>> = mutableMapOf()

	// build camera-specific projections only once per tick
	private val viewWorldMatrix: Matrix4 = Matrix4.of()
	private val clipWorldMatrix: Matrix4 = Matrix4.of()
	// full transformation matrix per entity
	private val clipLocalMatrix: Matrix4 = Matrix4.of()
	private val vec3 = Vector3.of()

	override fun update(entity: Entity, dt: Long) {
		pending.getOrPut(entity[Model::class.java].program) { mutableListOf() }.add((entity))
	}

	override fun after(dt: Long) {
		queue.cameras.forEach { camera ->
			// setup viewport
			camera.lens.let { lens ->
				renderBackend.viewport(lens.offset.x.toInt(), lens.offset.y.toInt(), lens.size.x.toInt(), lens.size.y.toInt())
			}

			// build the camera-specific clip <- world projection matrix
			clipWorldMatrix.apply {
				// init clip <- view projection matrix
				reset()
				scale(vec3.apply {
					// *2 to expand a [0, 1] range to [-1, 1]
					x = camera.lens.scale.x / camera.lens.size.x * 2
					y = camera.lens.scale.y / camera.lens.size.y * 2
					z = 1.0
				})
			}.multiply(viewWorldMatrix.apply {
				set(camera.transform.matrix)
				// camera offset is the negation of view offset
				xw *= -1
				yw *= -1
				zw *= -1
			})

			pending.forEach { (program, entities) ->
				entities.forEach {
					val transform = it[Transform::class.java]
					val model = it[Model::class.java]

					// build the entity-specific clip <- local projection matrix
					program.uniforms[0] = clipLocalMatrix.apply {
						set(clipWorldMatrix)
						// with model matrix
						multiply(transform.matrix)
						// with model offset - if any
						model.offset?.let(::multiply)
					}

					renderBackend.draw(program, model.meshes)
				}
			}
		}

		pending.clear()
	}
}
