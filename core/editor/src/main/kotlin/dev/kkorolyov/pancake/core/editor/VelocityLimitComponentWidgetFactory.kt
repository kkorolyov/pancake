package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.limit.VelocityLimit
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.dragInput
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component

class VelocityLimitComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<VelocityLimit>(t) {
		Widget {
			dragInput("##linear", linear, min = 0.0) { linear = it }
			tooltip("linear velocity limit (m/s)")

			dragInput("##angular", angular, min = 0.0) { angular = it }
			tooltip("angular velocity limit (rad/s)")
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<VelocityLimit>(c, onNew) {
		var linear = 0.0
		var angular = 0.0

		Widget {
			dragInput("##linear", linear, min = 0.0) { linear = it }
			tooltip("linear velocity limit (m/s)")

			dragInput("##angular", angular, min = 0.0) { angular = it }
			tooltip("angular velocity limit (rad/s)")

			disabledIf(linear < 0.0 || angular < 0.0) {
				button("apply") { it(VelocityLimit(linear, angular)) }
			}
		}
	}
}
