package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.factory.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.factory.getActionWidget
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.menu
import dev.kkorolyov.pancake.editor.menuItem
import dev.kkorolyov.pancake.editor.widget.Modal
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.type.ImBoolean
import io.github.classgraph.ClassGraph

private val actionMinSize = Vector2.of(200.0, 100.0)

private val actionTypes by ThreadLocal.withInitial {
	ClassGraph().enableClassInfo().scan().use { result ->
		result.getClassesImplementing(Action::class.java)
			.filter { !it.isAbstract && !it.isInterface }
			.map { it.loadClass(Action::class.java) }
	}
}

private val noopModal = Modal("noop", Widget {}, ImBoolean(false))

class ActionQueueComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(t: Component): Widget? = ComponentWidgetFactory.get<ActionQueue>(t) {
		var create: Modal = noopModal

		Widget {
			list("##data") {
				contextMenu(true) {
					menu("add") {
						actionTypes.forEach { type ->
							menuItem(type.simpleName) {
								create = Modal(
									"New ${type.simpleName}",
									getActionWidget(type) {
										create.visible = false
										add(it)
									},
									minSize = actionMinSize
								)
							}
						}
					}
				}

				forEach {
					getActionWidget(it)()
				}
			}

			create()
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get(c, onNew) {
		Widget {
			it(ActionQueue())
		}
	}
}
