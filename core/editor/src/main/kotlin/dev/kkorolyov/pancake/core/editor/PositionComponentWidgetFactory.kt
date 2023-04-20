package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3

class PositionComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Position>(t) {
		Widget {
			input3("##value", value) { value.set(it) }
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Component, Position>(c, onNew) {
		val value = Vector3.of()

		Widget {
			input3("##value", value) { value.set(it) }
			tooltip("value")
			button("apply") { it(Position(value)) }
		}
	}
}
