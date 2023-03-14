package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.Damping
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.input
import dev.kkorolyov.pancake.platform.entity.Component

class DampingComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Damping>(component) {
		value.input("##value")
	}
}
