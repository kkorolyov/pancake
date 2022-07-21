package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.input
import dev.kkorolyov.pancake.editor.ext.readonly
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.entity.Component

class TransformComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Transform>(component) {
		tree("Position") {
			position.input()
		}
		tree("Global Position") {
			globalPosition.readonly()
		}
	}
}
