package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.Force
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3

class ForceComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Force>(t) {
		Widget {
			input3("##value", value) { value.set(it) }
			tooltip("value (N)")

			input3("##offset", offset) { offset.set(it) }
			tooltip("offset (m)")
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Force>(c, onNew) {
		val value = Vector3.of()
		val offset = Vector3.of()

		Widget {
			input3("##value", value) { value.set(it) }
			tooltip("value (N)")

			input3("##offset", offset) { offset.set(it) }
			tooltip("offset (m)")

			button("apply") {
				it(Force().apply {
					this.value.set(value)
				})
			}
		}
	}
}
