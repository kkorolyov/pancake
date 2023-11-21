package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.child
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.menu
import dev.kkorolyov.pancake.editor.menuItem
import dev.kkorolyov.pancake.editor.onDrag
import dev.kkorolyov.pancake.editor.onDrop
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.setDragDropPayload
import dev.kkorolyov.pancake.editor.useDragDropPayload
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Vector2
import io.github.classgraph.ClassGraph

private val componentMinSize = Vector2.of(200.0, 100.0)

private val componentTypes by ThreadLocal.withInitial {
	ClassGraph().enableClassInfo().scan().use { result ->
		result.getClassesImplementing(Component::class.java)
			.filter { !it.isAbstract && !it.isInterface }
			.map { it.loadClass(Component::class.java) }
	}
}

/**
 * Displays and provides for modification of [entity] properties.
 * Submits expanded component windows by component to [componentManifest], which is expected to be rendered externally.
 * This is to avoid feedback loops with docking dependent windows together.
 */
class EntityDetails(private val entity: Entity, private val componentManifest: WindowManifest<Component>) : Widget {
	private val current = DebouncedValue<Component, Widget> { getWidget(Component::class.java, it) }

	private val inlineDetails = Popup("inlineDetails")

	private var create: Modal? = null
	private var createContent: Widget? = null

	private var toAdd: Component? = null
	private var toRemove: Class<out Component>? = null

	override fun invoke() {
		child("components", width = 200f, height = 200f) {
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
					onDrag {
						setDragDropPayload(it, "component")
						current.set(it)()
					}
				}

				selectable("##empty") {}
				contextMenu {
					drawAddMenu()
				}
				inlineDetails()
			}
		}
		onDrop {
			useDragDropPayload<Component>("component") {
				componentManifest[it] = { Window("Entity ${entity.id}: ${it::class.simpleName}", getWidget(Component::class.java, it), minSize = componentMinSize, openAt = OpenAt.Cursor) }
			}
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
						create = Modal(
							"New ${type.simpleName}",
							minSize = componentMinSize
						)
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
