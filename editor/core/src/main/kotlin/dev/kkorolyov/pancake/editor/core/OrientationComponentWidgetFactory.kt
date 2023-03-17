package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Orientation
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component

class OrientationComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Orientation>(component) {
		input3("##value", value) { value.set(it) }
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get<Orientation>(c, onNew) {
		text("TODO Orientation")
	}
}
