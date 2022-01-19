package dev.kkorolyov.pancake.graphics.jfx.system

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.graphics.jfx.Camera
import dev.kkorolyov.pancake.graphics.jfx.CameraCreated
import dev.kkorolyov.pancake.graphics.jfx.CameraDestroyed
import dev.kkorolyov.pancake.graphics.jfx.component.Graphic
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.utility.Limiter
import javafx.animation.AnimationTimer
import javafx.application.Platform
import javafx.scene.canvas.Canvas
import javafx.scene.transform.Affine

/**
 * Draws entities according to all known [Camera]s.
 */
class DrawSystem : GameSystem(
	listOf(Transform::class.java, Graphic::class.java),
	Limiter.fromConfig(DrawSystem::class.java)
) {
	private val renderers: MutableMap<Int, Renderer> = mutableMapOf()
	private val pending: MutableList<Entity> = mutableListOf()

	override fun attach() {
		register(CameraCreated::class.java) {
			val renderer = Renderer(it.camera)
			renderers[it.camera.id] = renderer
			Platform.runLater { renderer.start() }
		}
		register(CameraDestroyed::class.java) {
			renderers.remove(it.id)?.let {
				Platform.runLater { it.stop() }
			}
		}
	}

	override fun before(dt: Long) {
		pending.clear()
	}

	override fun update(entity: Entity, dt: Long) {
		pending.add(entity)
	}

	override fun after(dt: Long) {
		pending.sortBy { it.get(Transform::class.java).globalPosition.z }
		renderers.values.forEach { it.draw(pending) }
		renderers.values.forEach(Renderer::commit)
	}
}

private class Renderer(private val camera: Camera) : AnimationTimer() {
	private val affine: Affine = Affine()
	// TODO reenable if possible to avoid snapshotting on FX thread
//	private val buffer: Canvas = Canvas().also {
//		// sync buffer with rendered canvas
//		val canvas = camera.lens.canvas
//		it.widthProperty().bind(canvas.widthProperty())
//		it.heightProperty().bind(canvas.heightProperty())
//
//		// drop stale images and allow regen for current size
//		val changeListener: ChangeListener<Number> = ChangeListener { _, _, _ -> image = null }
//		it.widthProperty().addListener(changeListener)
//		it.heightProperty().addListener(changeListener)
//	}
//
//	private val snapshotParameters: SnapshotParameters = SnapshotParameters().also {
//		it.fill = Color.TRANSPARENT
//	}
//	private var image: WritableImage? = null
//	private var ref: AtomicReference<WritableImage> = AtomicReference()

	fun draw(entities: List<Entity>) {
		val canvas: Canvas = camera.lens.canvas
		val scale: Vector2 = camera.lens.scale

		canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)

		entities.forEach { entity ->
			val graphic = entity.get(Graphic::class.java)
			val position = camera.map(entity.get(Transform::class.java).globalPosition)
			// TODO rotate

			canvas.graphicsContext2D.let { g ->
				g.save()

				affine.appendTranslation(canvas.width / 2, canvas.height / 2)
				affine.appendTranslation(position.x, position.y)
				affine.appendScale(scale.x, scale.y)
				// TODO scale by transform
				g.transform(affine)
				graphic(g)

				affine.setToIdentity()
				g.restore()
			}
		}
	}

	fun commit() {
		// FIXME only able to snapshot on FX thread kinda defeats the purpose of the buffer
//		Platform.runLater {
//			image = buffer.snapshot(snapshotParameters, ref.getAndSet(null))
//			ref.set(image)
//		}
	}

	override fun handle(now: Long) {
//		ref.get()?.let {
//			val canvas = camera.lens.canvas
//
//			canvas.graphicsContext2D.let { g ->
//				g.clearRect(0.0, 0.0, canvas.width, canvas.height)
//				g.drawImage(it, 0.0, 0.0)
//			}
//		}
	}
}
