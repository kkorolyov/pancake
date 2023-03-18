package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component

class PositionComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(t: Component): Widget? = ComponentWidgetFactory.get<Position>(t) {
		Widget {
			input3("##value", value) { value.set(it) }
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get<Position>(c, onNew) {
		Widget {
			text("TODO Position")
		}
	}
}
