package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.limit.VelocityLimit
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.platform.entity.Component

class VelocityLimitComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<VelocityLimit>(component) {
		input("##value", value) { value = it }
	}
}
