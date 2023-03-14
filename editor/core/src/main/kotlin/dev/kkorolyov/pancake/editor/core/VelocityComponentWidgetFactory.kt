package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.Velocity
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.input
import dev.kkorolyov.pancake.platform.entity.Component

class VelocityComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Velocity>(component) {
		value.input("##value")
	}
}
