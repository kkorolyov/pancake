package dev.kkorolyov.ponk

import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.core.component.media.Graphic
import dev.kkorolyov.pancake.platform.Resources
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.event.EventBroadcaster
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.media.graphic.Renderable
import dev.kkorolyov.pancake.platform.media.graphic.shape.Shape.Color
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory
import dev.kkorolyov.pancake.platform.registry.Registry
import dev.kkorolyov.pancake.platform.registry.ResourceReader

private fun <T, F : DeferredConverterFactory<T>> load(
	path: String,
	strat: Class<F>
): Registry<String, T> =
	Resources.`in`(path).orElse(null).use {
		Registry<String, T>().apply {
			load(ResourceReader(DeferredConverterFactory.get(strat)).fromYaml(it))
		}
	}

val events: EventBroadcaster.Managed = EventBroadcaster.Managed()

private val paddle: Renderable = Resources.RENDER_MEDIUM.box.apply {
	fill = Color.BLACK
	size.set(1.0, 8.0)
}
private val paddleBounds: Vector = Vector(1.0, 8.0)
val entities: EntityPool = EntityPool(events).apply {
	val player = create().apply {
		add(Graphic(paddle))
		add(Bounds(paddleBounds))
		add(Transform(Vector(.5, 4.0)))
	}
	val opponent = create().apply {
		add(Graphic(paddle))
		add(Bounds(paddleBounds))
		add(Transform(Vector(3.5, 4.0)))
	}
}
