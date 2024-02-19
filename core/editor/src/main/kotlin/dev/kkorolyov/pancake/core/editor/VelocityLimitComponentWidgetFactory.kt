package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.limit.VelocityLimit
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component

class VelocityLimitComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<VelocityLimit>(t) {
		Widget {
			input("##linear", linear) { linear = it }
			tooltip("linear velocity limit (m/s)")

			input("##angular", angular) { angular = it }
			tooltip("angular velocity limit (rad/s)")
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<VelocityLimit>(c, onNew) {
		var linear = 0.0
		var angular = 0.0

		Widget {
			input("##linear", linear) { linear = it }
			tooltip("linear velocity limit (m/s)")

			input("##angular", angular) { angular = it }
			tooltip("angular velocity limit (rad/s)")

			disabledIf(linear < 0.0 || angular < 0.0) {
				button("apply") { it(VelocityLimit(linear, angular)) }
			}
		}
	}
}
