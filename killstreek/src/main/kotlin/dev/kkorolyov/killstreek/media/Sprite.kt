package dev.kkorolyov.killstreek.media

import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.media.Animated
import dev.kkorolyov.pancake.platform.media.CompositeImage
import dev.kkorolyov.pancake.platform.media.Renderable
import javafx.scene.canvas.GraphicsContext

/**
 * A dynamic image.
 */
class Sprite(
		/** sprite sheet */
		private val image: CompositeImage,
		/** angle vector used for padding sprite orientation calculation */
		private val orientationOffset: Vector,
		/** number of frames in {@code image} along x-axis */
		xFrames: Int = 1,
		/** number of frames in {@code image} along y-axis */
		yFrames: Int = 1,
		/** {@code ns} between frame changes */
		private var frameInterval: Long = 0
) : Renderable, Animated {
	private val origin = Vector()
	private val frames = Vector(xFrames.toDouble(), yFrames.toDouble())
	private val frameSize = Vector(image.size.x / frames.x, image.size.y / frames.y)

	private var currentFrameTime: Long = 0
	private var frame: Int = 0
	private var isActive: Boolean = true

	override fun render(g: GraphicsContext, position: Vector) {
		for (layer in image) {
			g.drawImage(
					layer, origin.x, origin.y, frameSize.x, frameSize.y,
					position.x, position.y, frameSize.x, frameSize.y
			)
		}
	}

	override fun size(): Vector = frameSize
	override fun getOrientationOffset(): Vector = orientationOffset

	override fun tick(dt: Long) {
		if (!isActive) return

		currentFrameTime += dt
		if (currentFrameTime < Math.abs(frameInterval)) return

		frame += Math.round((currentFrameTime / frameInterval).toDouble()).toInt()  // Reversed if negative
		currentFrameTime = 0
	}

	override fun toggle() {
		isActive = !isActive
	}

	override fun isActive(): Boolean = isActive && frameInterval != 0L
	override fun setActive(active: Boolean) {
		isActive = active
	}

	override fun getFrame(): Int = frame
	override fun setFrame(frame: Int) {
		this.frame = Math.floorMod(frame, length())

		origin.set(
				(frame % frames.x).toInt() * frameSize.x,
				(frame / frames.x) * frameSize.y
		)
	}

	override fun getFrameInterval(): Long = frameInterval
	override fun setFrameInterval(frameInterval: Long) {
		this.frameInterval = frameInterval
	}

	override fun length(): Int = (frames.x * frames.y).toInt()
}
