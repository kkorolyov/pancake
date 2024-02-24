package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.Mass
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.dragInput
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component

class MassComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Mass>(t) {
		Widget {
			dragInput("##value", value, min = 0.0) { value = it }
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Mass>(c, onNew) {
		var value = 0.0

		Widget {
			dragInput("##value", value, min = 0.0) { value = it }
			tooltip("value")
			disabledIf(value < 0.0) {
				button("apply") { it(Mass(value)) }
			}
		}
	}
}
