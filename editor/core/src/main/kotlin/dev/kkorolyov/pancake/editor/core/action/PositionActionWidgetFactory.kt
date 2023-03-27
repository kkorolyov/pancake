package dev.kkorolyov.pancake.editor.core.action

import dev.kkorolyov.pancake.core.action.PositionAction
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.factory.ActionWidgetFactory
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.math.Vector3

class PositionActionWidgetFactory : ActionWidgetFactory {
	override fun get(t: Action): Widget? = ActionWidgetFactory.get<PositionAction>(t) {
		Widget { text(this) }
	}

	override fun get(c: Class<Action>, onNew: (Action) -> Unit): Widget? = ActionWidgetFactory.get(c, onNew) {
		val value = Vector3.of()

		Widget {
			input3("##value", value) { value.set(it) }
			tooltip("value")
			button("apply") { it(PositionAction(value)) }
		}
	}
}
