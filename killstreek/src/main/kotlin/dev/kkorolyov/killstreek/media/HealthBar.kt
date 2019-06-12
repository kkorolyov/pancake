package dev.kkorolyov.killstreek.media

import dev.kkorolyov.killstreek.component.Health
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.media.Renderable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

private val ORIENTATION_OFFSET = Vector()

/**
 * A rendered health bar.
 */
class HealthBar(
		/** rendered health */
		val health: Health,
		/** bar size in {@code px} */
		val size: Vector
) : Renderable {
	override fun render(g: GraphicsContext, position: Vector) {
		val initialFill = g.fill
		val initialStroke = g.stroke
		val initialWidth = g.lineWidth

		val x = position.x.toDouble()
		val y = position.y.toDouble()
		val w = (size.x * health.percentage).toDouble()
		val h = size.y.toDouble()

		g.fill = Color.RED
		g.fillRect(x, y, w, h)

		g.stroke = Color.DARKGRAY
		g.lineWidth = 2.0
		g.strokeRect(x, y, w, h)

		g.fill = initialFill
		g.stroke = initialStroke
		g.lineWidth = initialWidth
	}

	override fun size(): Vector = size

	override fun getOrientationOffset(): Vector = ORIENTATION_OFFSET
}
