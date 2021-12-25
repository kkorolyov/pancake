package dev.kkorolyov.pancake.graphics.jfx.system

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.graphics.jfx.AddCamera
import dev.kkorolyov.pancake.graphics.jfx.Camera
import dev.kkorolyov.pancake.graphics.jfx.RemoveCamera
import dev.kkorolyov.pancake.graphics.jfx.component.Graphic
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.utility.Limiter
import javafx.animation.AnimationTimer
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.scene.SnapshotParameters
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.transform.Affine
import java.util.concurrent.atomic.AtomicReference

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

	private val renderers: MutableMap<Camera, Renderer> = mutableMapOf()
	private val pending: MutableList<Entity> = mutableListOf()

	override fun attach() {
		register(AddCamera::class.java) {
			renderers[it.camera] = Renderer(pane).also { renderer ->
				renderer.start()
			}
		}
		register(RemoveCamera::class.java) {
			renderers.remove(it.camera)?.let { renderer ->
				renderer.destroy()
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
		renderers.forEach { (camera, renderer) ->
			renderer.drawContext.clearRect(0.0, 0.0, renderer.width, renderer.height)

			pending.forEach { entity ->
				val graphic = entity.get(Graphic::class.java)
				val position = camera.map(entity.get(Transform::class.java).globalPosition)
				// TODO rotate

				renderer.drawContext.let { g ->
					g.save()

					affine.appendTranslation(renderer.width / 2, renderer.height / 2)
					affine.appendTranslation(position.x, position.y)
					affine.appendScale(camera.scale.x, camera.scale.y)
					// TODO scale by transform
					g.transform(affine)
					graphic(g)

					affine.setToIdentity()
					g.restore()
				}
				renderer.commit()
			}
		}
	}
}

private class Renderer(private val pane: Pane) : AnimationTimer() {
	private val canvas: CanvasPane = CanvasPane().also {
		Platform.runLater { pane.children += it }
	}

	private val draw: Canvas = Canvas().also {
		it.widthProperty().bind(canvas.widthProperty())
		it.heightProperty().bind(canvas.heightProperty())

		val changeListener: ChangeListener<Number> = ChangeListener { _, _, _ -> image = null }
		it.widthProperty().addListener(changeListener)
		it.heightProperty().addListener(changeListener)
	}
	private val snapshotParameters: SnapshotParameters = SnapshotParameters().also {
		it.fill = Color.TRANSPARENT
	}
	private var image: WritableImage? = null
	private var buffer: AtomicReference<Image> = AtomicReference()

	val drawContext: GraphicsContext
		get() = draw.graphicsContext2D
	val width
		get() = canvas.width
	val height
		get() = canvas.height

	fun destroy() {
		stop()
		Platform.runLater { pane.children -= canvas }
	}

	fun commit() {
		Platform.runLater {
			image = draw.snapshot(snapshotParameters, image)
			buffer.set(image)
		}
	}

	override fun handle(now: Long) {
		buffer.getAndSet(null)?.let(canvas::draw)
	}
}

private class CanvasPane : Pane() {
	private val canvas: Canvas = Canvas().also {
		children += it
		it.widthProperty().bind(widthProperty())
		it.heightProperty().bind(heightProperty())

		prefWidth = Double.MAX_VALUE
		prefHeight = Double.MAX_VALUE
	}

	fun draw(image: Image) {
		canvas.graphicsContext2D.clearRect(0.0, 0.0, width, height)
		canvas.graphicsContext2D.drawImage(image, 0.0, 0.0)
	}
}
