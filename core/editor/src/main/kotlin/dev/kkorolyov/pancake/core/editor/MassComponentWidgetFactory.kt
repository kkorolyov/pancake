package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.movement.Mass
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component

private const val step = 0.001
private const val stepFast = 1.0

class MassComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Mass>(t) {
		Widget {
			input("##value", value, step = step, stepFast = stepFast) { value = it }
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Component, Mass>(c, onNew) {
		var value = 0.0

		Widget {
			input("##value", value, step = step, stepFast = stepFast) { value = it }
			tooltip("value")
			disabledIf(value < 0.0) {
				button("apply") { it(Mass(value)) }
			}
		}
	}
}
