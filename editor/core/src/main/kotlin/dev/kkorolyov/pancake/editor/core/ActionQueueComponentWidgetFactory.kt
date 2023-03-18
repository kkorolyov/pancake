package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.factory.getActionWidget
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component

class ActionQueueComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(t: Component): Widget? = ComponentWidgetFactory.get<ActionQueue>(t) {
		list("##data") {
			forEach {
				getActionWidget(it)()
			}
		}
		// TODO support adding actions
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get<ActionQueue>(c, onNew) {
		text("TODO ActionQueue")
	}
}
