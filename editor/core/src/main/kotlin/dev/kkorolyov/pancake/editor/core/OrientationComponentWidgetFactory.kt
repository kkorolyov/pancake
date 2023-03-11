package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Orientation
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.input
import dev.kkorolyov.pancake.editor.ext.readonly
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.entity.Component

class OrientationComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Orientation>(component) {
		tree("Value") {
			value.input()
		}
		tree("Global Value") {
			globalValue.readonly()
		}
	}
}
