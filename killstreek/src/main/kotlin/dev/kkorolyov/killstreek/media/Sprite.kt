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
		orientationOffset: Vector,
		/** number of frames in {@code image} along x-axis */
		xFrames: Int = 1,
		/** number of frames in {@code image} along y-axis */
		yFrames: Int = 1,
		/** {@code ns} between frame changes */
		frameInterval: Long = 0
) : Renderable, Animated {
	private val origin = Vector()
	private val frames = Vector(xFrames.toFloat(), yFrames.toFloat())
	private val frameSize = Vector(image.size.x / frames.x, image.size.y / frames.y)

	private var _frameInterval: Long = frameInterval
	private var currentFrameTime: Long = 0
	private var _frame: Int = 0
	private var _active: Boolean = true

	private val _orientationOffset = orientationOffset

	override fun render(g: GraphicsContext, position: Vector) {
		for (layer in image) {
			g.drawImage(
					layer, origin.x.toDouble(), origin.y.toDouble(), frameSize.x.toDouble(), frameSize.y.toDouble(),
					position.x.toDouble(), position.y.toDouble(), frameSize.x.toDouble(), frameSize.y.toDouble()
			)
		}
	}

	override fun size(): Vector = frameSize
	override fun getOrientationOffset(): Vector = _orientationOffset

	override fun tick(dt: Long) {
		if (!isActive) return

		currentFrameTime += dt
		if (currentFrameTime < Math.abs(frameInterval)) return

		frame += Math.round(currentFrameTime.toDouble() / frameInterval.toDouble()).toInt()  // Reversed if negative
		currentFrameTime = 0
	}

	override fun toggle() {
		isActive = !isActive
	}

	override fun isActive(): Boolean = _active && frameInterval != 0L
	override fun setActive(active: Boolean) {
		_active = active
	}

	override fun getFrame(): Int = _frame
	override fun setFrame(frame: Int) {
		_frame = Math.floorMod(frame, length())

		origin.set(
				(_frame % frames.x).toInt() * frameSize.x,
				(_frame / frames.x) * frameSize.y
		)
	}

	override fun getFrameInterval(): Long = _frameInterval
	override fun setFrameInterval(frameInterval: Long) {
		_frameInterval = frameInterval
	}

	override fun length(): Int = (frames.x * frames.y).toInt()
}
