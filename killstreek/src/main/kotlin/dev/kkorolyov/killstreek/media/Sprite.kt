package dev.kkorolyov.killstreek.media

import dev.kkorolyov.pancake.platform.media.Animated
import dev.kkorolyov.pancake.platform.media.graphic.CompositeRenderable
import dev.kkorolyov.pancake.platform.media.graphic.Image
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform
import dev.kkorolyov.pancake.platform.media.graphic.Renderable
import dev.kkorolyov.pancake.platform.media.graphic.Viewport

/**
 * A dynamic image.
 */
class Sprite(
		/** layered sprite sheets */
		private val sheets: CompositeRenderable<Image>,
		/** sprite sheet partioning scheme */
		private val viewport: Viewport,
		/** {@code ns} between frame changes */
		private var frameInterval: Long = 0
) : Renderable, Animated {
	private var currentFrameTime: Long = 0
	private var frame: Int = 0
	private var isActive: Boolean = true

	init {
		sheets.forEach { it.viewport = viewport }
	}

	override fun render(transform: RenderTransform) {
		sheets.render(transform)
	}

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
		viewport.set(this.frame)
	}

	override fun getFrameInterval(): Long = frameInterval
	override fun setFrameInterval(frameInterval: Long) {
		this.frameInterval = frameInterval
	}

	override fun length(): Int = viewport.length()
}
