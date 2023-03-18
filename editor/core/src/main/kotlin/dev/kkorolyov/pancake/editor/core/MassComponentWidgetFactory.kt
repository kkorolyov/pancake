package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.Mass
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component

class MassComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(t: Component): Widget? = ComponentWidgetFactory.get<Mass>(t) {
		Widget {
			input("##value", value, step = 0.001, stepFast = 1.0) { value = it }
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get<Mass>(c, onNew) {
		Widget {
			text("TODO Mass")
		}
	}
}
