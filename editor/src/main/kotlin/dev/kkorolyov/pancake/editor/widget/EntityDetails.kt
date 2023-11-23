package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.menu
import dev.kkorolyov.pancake.editor.menuItem
import dev.kkorolyov.pancake.editor.onDrag
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.setDragDropPayload
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import io.github.classgraph.ClassGraph

private val componentTypes by ThreadLocal.withInitial {
	ClassGraph().enableClassInfo().scan().use { result ->
		result.getClassesImplementing(Component::class.java)
			.filter { !it.isAbstract && !it.isInterface }
			.map { it.loadClass(Component::class.java) }
	}
}

/**
 * Displays and provides for modification of [entity] properties.
 * If [dragDropId] is provided, emits drag-drop payloads to it containing the selected [Component].
 */
class EntityDetails(private val entity: Entity, private val dragDropId: String? = null) : Widget {
	private val current = DebouncedValue<Component, Widget> { getWidget(Component::class.java, it) }

	private val inlineDetails = Popup("inlineDetails")

	private var create: Modal? = null
	private var createContent: Widget? = null

	private var toAdd: Component? = null
	private var toRemove: Class<out Component>? = null

	override fun invoke() {
		list("##components") {
			entity.forEach {
				selectable(it::class.simpleName ?: it::class) {
					inlineDetails.open(current.set(it))
				}
				contextMenu {
					drawAddMenu()
					menuItem("remove") {
						toRemove = it::class.java
					}
				}

				dragDropId?.let { id ->
					onDrag {
						setDragDropPayload(it, id)
						current.set(it)()
					}
				}
			}

			selectable("##empty") {}
			contextMenu {
				drawAddMenu()
			}
			inlineDetails()
		}

		// augment elements only after done iterating
		toAdd?.let {
			entity.put(it)
			toAdd = null
		}
		toRemove?.let {
			entity.remove(it)
			toRemove = null
		}

		// open outside the menu creating the modal because ID stack
		createContent?.let {
			create?.open(it)
			createContent = null
		}
		create?.invoke()
	}

	private fun drawAddMenu() {
		menu("add") {
			componentTypes.forEach { type ->
				if (entity[type] == null) {
					menuItem(type.simpleName) {
						create = Modal("New ${type.simpleName}")
						createContent = getWidget(Component::class.java, type) {
							create?.close()
							toAdd = it
						}
					}
				}
			}
		}
	}
}
