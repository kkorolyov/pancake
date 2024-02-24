package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.Damping
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.dragInput
import dev.kkorolyov.pancake.editor.dragInput3
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3

class DampingComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Damping>(t) {
		Widget {
			dragInput3("##linear", linear, min = 0.0, max = 1.0, speed = 0.001f) { linear.set(it) }
			tooltip("linear value")

			dragInput3("##angular", angular, min = 0.0, max = 1.0, speed = 0.001f) { angular.set(it) }
			tooltip("angular value")
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Damping>(c, onNew) {
		var linear = 0.0
		var angular = 0.0

		Widget {
			dragInput("##linear", linear, min = 0.0, max = 1.0, speed = 0.001f) { linear = it }
			tooltip("linear value")

			dragInput("##angular", angular, min = 0.0, max = 1.0, speed = 0.001f) { angular = it }
			tooltip("linear value")

			disabledIf(linear < 0.0 || linear > 1.0 || angular < 0.0 || angular > 1.0) {
				button("apply") { it(Damping(Vector3.of(linear, linear, linear), Vector3.of(angular, angular, angular))) }
			}
		}
	}
}
