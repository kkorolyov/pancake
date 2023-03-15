package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component

class ActionQueueComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<ActionQueue>(component) {
		list("##data") {
			forEach(::text)
		}
	}
}
