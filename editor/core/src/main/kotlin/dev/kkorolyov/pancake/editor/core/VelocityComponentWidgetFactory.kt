package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.Velocity
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component

class VelocityComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Velocity>(component) {
		input3("##value", value) { value.set(it) }
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get<Velocity>(c, onNew) {
		text("TODO Velocity")
	}
}