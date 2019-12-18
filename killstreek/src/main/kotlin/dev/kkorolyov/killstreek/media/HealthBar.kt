package dev.kkorolyov.killstreek.media

import dev.kkorolyov.killstreek.component.Health
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.media.graphic.RenderMedium
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform
import dev.kkorolyov.pancake.platform.media.graphic.Renderable
import dev.kkorolyov.pancake.platform.media.graphic.shape.Box
import dev.kkorolyov.pancake.platform.media.graphic.shape.Shape.Color

/**
 * A rendered health bar.
 */
class HealthBar(
		/** rendered health */
		val health: Health,
		/** bar size in {@code px} */
		val size: Vector,
		/** medium to render on */
		renderMedium: RenderMedium
) : Renderable {
	private val box: Box = renderMedium.box.apply {
		fill = Color.RED
		stroke = Color.GRAY
		strokeWidth = 2.0
	}

	override fun render(transform: RenderTransform) {
		box.size.set(size.x * health.percentage, size.y)
		box.render(transform)
	}
}
