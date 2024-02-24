package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.dragInput3
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3

class TransformComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Transform>(t) {
		Widget {
			dragInput3("##translation", translation) { translation.set(it) }
			tooltip("translation")

			dragInput3("##scale", scale) { scale.set(it) }
			tooltip("scale")
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Transform>(c, onNew) {
		val translation = Vector3.of()
		val scale = Vector3.of(1.0, 1.0, 1.0)

		Widget {
			dragInput3("##translation", translation) { translation.set(it) }
			tooltip("translation")

			dragInput3("##scale", scale) { scale.set(it) }
			tooltip("scale")

			button("apply") {
				it(Transform().apply {
					this.translation.set(translation)
					this.scale.set(scale)
				})
			}
		}
	}
}
