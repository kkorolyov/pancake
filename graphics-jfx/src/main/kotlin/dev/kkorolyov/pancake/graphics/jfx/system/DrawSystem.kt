package dev.kkorolyov.pancake.graphics.jfx.system

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.utility.Limiter
import dev.kkorolyov.pancake.graphics.jfx.AddCamera
import dev.kkorolyov.pancake.graphics.jfx.Camera
import dev.kkorolyov.pancake.graphics.jfx.RemoveCamera
import dev.kkorolyov.pancake.graphics.jfx.component.Graphic
import javafx.application.Platform
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import javafx.scene.transform.Affine

/**
 * Draws entities on canvases in some [pane].
 * [AddCamera] events append a new canvas for the given camera to the pane.
 * [RemoveCamera] events remove the canvas associated with the given camera from the pane.
 */
class DrawSystem(private val pane: Pane) : GameSystem(
	Signature(Transform::class.java, Graphic::class.java),
	Limiter.fromConfig(DrawSystem::class.java)
) {
	private val affine: Affine = Affine()

	private val canvases: MutableMap<Camera, Canvas> = mutableMapOf()
	private val pending: MutableList<Entity> = mutableListOf()

	override fun attach() {
		register(AddCamera::class.java) {
			val canvas = Canvas()

			canvases[it.camera] = canvas

			canvas.widthProperty().bind(pane.widthProperty())
			canvas.heightProperty().bind(pane.heightProperty())

			Platform.runLater { pane.children += canvas }
		}
		register(RemoveCamera::class.java) {
			canvases.remove(it.camera)?.let { canvas ->
				canvas.widthProperty().unbind()
				canvas.heightProperty().unbind()

				Platform.runLater { pane.children -= canvas }
			}
		}
	}

	override fun before(dt: Long) {
		pending.clear()
	}

	override fun update(entity: Entity, dt: Long) {
		if (pane.width > 0.0 && pane.height > 0.0) pending.add(entity)
	}

	override fun after(dt: Long) {
		pending.sortBy { it.get(Transform::class.java).globalPosition.z }
		canvases.forEach { (camera, canvas) ->
			canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)

			pending.forEach { entity ->
				val graphic = entity.get(Graphic::class.java)
				val position = camera.map(entity.get(Transform::class.java).globalPosition)
				// TODO rotate

				canvas.graphicsContext2D.let { g ->
					g.save()

					affine.appendTranslation(canvas.width / 2, canvas.height / 2)
					affine.appendTranslation(position.x, position.y)
					affine.appendScale(camera.scale.x, camera.scale.y)
					// TODO scale by transform
					g.transform(affine)
					graphic(g)

					affine.setToIdentity()
					g.restore()
				}
			}
		}
	}
}
