package dev.kkorolyov.killstreek.media

import dev.kkorolyov.killstreek.component.Health
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform
import dev.kkorolyov.pancake.platform.media.graphic.Renderable
import dev.kkorolyov.pancake.platform.media.graphic.shape.Box
import dev.kkorolyov.pancake.platform.media.graphic.shape.Shape.Color
import dev.kkorolyov.pancake.platform.plugin.RenderMedium

/**
 * A rendered health bar.
 */
class HealthBar(
	/** rendered health */
	private val health: Health,
	/** bar size in {@code px} */
	private val size: Vector2,
	/** medium to render on */
	renderMedium: RenderMedium
) : Renderable {
	private val box: Box = renderMedium.box.apply {
		fill = Color.RED
		stroke = Color.GRAY
		strokeWidth = 2.0
	}

	override fun render(transform: RenderTransform) {
		box.size.let {
			it.x = size.x * health.percentage
			it.y = size.y
		}
		box.render(transform)
	}
}
