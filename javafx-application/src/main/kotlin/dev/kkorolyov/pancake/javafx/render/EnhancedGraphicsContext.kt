package dev.kkorolyov.pancake.javafx.render

import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform
import dev.kkorolyov.pancake.platform.media.graphic.shape.Shape
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.transform.Rotate

/**
 * A [GraphicsContext] wrapper providing additional stateful functionality.
 * Clients must apply stateful augmentations to the wrapped graphics context solely through this interface.
 * These augmentations are retained only until the next [get], ensuring subsequent clients are unaffected by unexpected augmentations.
 */
class EnhancedGraphicsContext(private val g: GraphicsContext) {
	private val rotate: Rotate = Rotate()

	private var resetRotate: Boolean = false
	private var resetFill: Boolean = false
	private var resetStroke: Boolean = false
	private var resetLineWidth: Boolean = false

	/**
	 * Sets configuration according to [shape].
	 */
	fun shape(shape: Shape) {
		fill(shape.fill)
		stroke(shape.stroke)
		strokeWidth(shape.strokeWidth)
	}

	/** Sets current `fill` to [color]. */
	fun fill(color: Shape.Color?) {
		color?.run {
			g.fill = Color.rgb(red, green, blue, alpha / 255.0)

			resetFill = false
		}
	}

	/** Sets current `stroke` to [color]. */
	fun stroke(color: Shape.Color?) {
		color?.run {
			g.stroke = Color.rgb(red, green, blue, alpha / 255.0)

			resetStroke = false
		}
	}

	/** Sets current `strokeWidth` to [strokeWidth] in `px`. */
	fun strokeWidth(strokeWidth: Double?) {
		strokeWidth?.let {
			g.lineWidth = it

			resetLineWidth = false
		}
	}

	/** Sets current `rotation` to [transform]'s rotation. */
	fun rotate(transform: RenderTransform, pad: Boolean = false) {
		rotate(transform.rotation.z, transform.position, pad)

		resetRotate = false
	}

	private fun rotate(angle: Double, pivot: Vector?, pad: Boolean = false) {
		rotate.run {
			this.angle = -Math.toDegrees(if (pad) angle - (0.5 * Math.PI) else angle)

			pivotX = pivot?.x ?: 0.0
			pivotY = pivot?.y ?: 0.0

			g.setTransform(
				mxx, myx,
				mxy, myy,
				tx, ty
			)
		}
	}

	/** Returns wrapped [GraphicsContext] with any augmentations applied since the last call to this function. */
	fun get(): GraphicsContext {
		if (resetRotate) rotate(0.0, null)
		resetRotate = true

		if (resetFill) g.fill = Color.BLACK
		resetFill = true

		if (resetStroke) g.stroke = Color.BLACK
		resetStroke = true

		if (resetLineWidth) g.lineWidth = 1.0
		resetLineWidth = true

		return g
	}
}
