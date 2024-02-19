package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.Damping
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
			input3("##linear", linear) { linear.set(it) }
			tooltip("linear value")

			input3("##angular", angular) { angular.set(it) }
			tooltip("angular value")
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Damping>(c, onNew) {
		var linear = 0.0
		var angular = 0.0

		Widget {
			input("##linear", linear) { linear = it }
			tooltip("linear value")

			input("##angular", angular) { angular = it }
			tooltip("linear value")

			disabledIf(linear < 0.0 || linear > 1.0 || angular < 0.0 || angular > 1.0) {
				button("apply") { it(Damping(Vector3.of(linear, linear, linear), Vector3.of(angular, angular, angular))) }
			}
		}
	}
}
