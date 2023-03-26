package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.limit.VelocityLimit
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.factory.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component

class VelocityLimitComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(t: Component): Widget? = ComponentWidgetFactory.get<VelocityLimit>(t) {
		Widget {
			input("##value", value) { value = it }
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get(c, onNew) {
		var value = 0.0

		Widget {
			input("##value", value) { value = it }
			tooltip("value")
			disabledIf(value < 0.0) {
				button("apply") { it(VelocityLimit(value)) }
			}
		}
	}
}
