package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.movement.Damping
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3

class DampingComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Damping>(t) {
		Widget {
			input3("##value", value) { value.set(it) }
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Component, Damping>(c, onNew) {
		var value = 0.0

		Widget {
			input("##value", value) { value = it }
			tooltip("value")
			disabledIf(value < 0.0 || value > 1.0) {
				button("apply") { it(Damping(Vector3.of(value, value, value))) }
			}
		}
	}
}
