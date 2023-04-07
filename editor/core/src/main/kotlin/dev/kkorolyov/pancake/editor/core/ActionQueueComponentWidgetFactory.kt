package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.menu
import dev.kkorolyov.pancake.editor.menuItem
import dev.kkorolyov.pancake.editor.widget.Modal
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector2
import io.github.classgraph.ClassGraph

private val actionMinSize = Vector2.of(200.0, 100.0)

private val actionTypes by ThreadLocal.withInitial {
	ClassGraph().enableClassInfo().scan().use { result ->
		result.getClassesImplementing(Action::class.java)
			.filter { !it.isAbstract && !it.isInterface }
			.map { it.loadClass(Action::class.java) }
	}
}

class ActionQueueComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<ActionQueue>(t) {
		var create: Modal? = null

		Widget {
			list("##data") {
				contextMenu("data") {
					menu("add") {
						actionTypes.forEach { type ->
							menuItem(type.simpleName) {
								create = Modal(
									"New ${type.simpleName}",
									getWidget(Action::class.java, type) {
										create?.visible = false
										add(it)
									},
									minSize = actionMinSize
								)
							}
						}
					}
				}

				forEach {
					getWidget(Action::class.java, it)()
				}
			}

			create?.invoke()
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Component, ActionQueue>(c, onNew) {
		Widget {
			it(ActionQueue())
		}
	}
}
