package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.Mass
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.platform.entity.Component

class MassComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Mass>(component) {
		input("##value", value, step = 0.001, stepFast = 1.0) { value = it }
	}
}
