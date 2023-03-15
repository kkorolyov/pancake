package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.Force
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.platform.entity.Component

class ForceComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Force>(component) {
		input3("##value", value) { value.set(it) }
	}
}
