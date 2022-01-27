package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.editor.ComponentData
import dev.kkorolyov.pancake.editor.ComponentDataFactory
import dev.kkorolyov.pancake.editor.ComponentDetails
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Component
import javafx.collections.ObservableList
import tornadofx.listview
import tornadofx.observableListOf

class ActionQueueComponentDataFactory : ComponentDataFactory {
	override fun getData(component: Component): ComponentData<*, *>? =
		(component as? ActionQueue)?.let(::ActionQueueComponentData)

	override fun getDetails(data: ComponentData<*, *>): ComponentDetails? =
		(data as? ActionQueueComponentData)?.let(::ActionQueueComponentDetails)

	private data class ActionQueueComponentData(private val component: ActionQueue) :
		ComponentData<ActionQueue, ActionQueueComponentData>(component) {
		val actions: ObservableList<Action> = observableListOf()

		override fun refresh() {
			actions.setAll(component.toList())
		}

		override fun bind(other: ActionQueueComponentData?) {
			super.bind(other)
			other?.actions?.let(actions::setAll) ?: actions.clear()
		}
	}

	private class ActionQueueComponentDetails(model: ActionQueueComponentData) : ComponentDetails() {
		override val root = listview(model.actions) {
			maxHeight = 96.0
		}
	}
}
