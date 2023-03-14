package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.VelocityCap
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.input
import dev.kkorolyov.pancake.platform.entity.Component

class VelocityCapComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<VelocityCap>(component) {
		value.input("##value")
	}
}
