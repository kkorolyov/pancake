package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.Velocity
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3

class VelocityComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Velocity>(t) {
		Widget {
			input3("##linear", linear) { linear.set(it) }
			tooltip("linear velocity (m/s)")

			input3("##angular", angular) { angular.set(it) }
			tooltip("angular velocity (rad/s)")
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Velocity>(c, onNew) {
		val linear = Vector3.of()
		val angular = Vector3.of()

		Widget {
			input3("##linear", linear) { linear.set(it) }
			tooltip("linear velocity (m/s)")

			input3("##angular", angular) { angular.set(it) }
			tooltip("angular velocity (rad/s)")

			button("apply") {
				it(Velocity().apply {
					this.linear.set(linear)
					this.angular.set(angular)
				})
			}
		}
	}
}
