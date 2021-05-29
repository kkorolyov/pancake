package dev.kkorolyov.pancake.graphics.jfx.renderable

import dev.kkorolyov.pancake.platform.math.Vector2
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.transform.Affine

/**
 * A rectangle drawn according to [dimensions], [fill] color, and outline [stroke] color.
 */
class Rectangle(private val dimensions: Vector2, private val fill: Color, private val stroke: Color = fill) : Drawable {
	override fun draw(context: GraphicsContext, affine: Affine) {
		context.let {
			it.fill = fill
			it.stroke = stroke

			it.fillRect(
				// shift to upper left corner
				-dimensions.x / 2,
				-dimensions.y / 2,
				dimensions.x,
				dimensions.y
			)
			it.strokeRect(
				// shift to upper left corner
				-dimensions.x / 2,
				-dimensions.y / 2,
				dimensions.x,
				dimensions.y
			)
		}
	}
}
