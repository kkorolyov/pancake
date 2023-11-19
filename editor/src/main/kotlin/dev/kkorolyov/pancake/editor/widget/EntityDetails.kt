package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.MemoizedContent
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.menu
import dev.kkorolyov.pancake.editor.menuItem
import dev.kkorolyov.pancake.editor.onDoubleClick
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.flag.ImGuiSelectableFlags
import io.github.classgraph.ClassGraph
import org.lwjgl.glfw.GLFW
import kotlin.reflect.KClass

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
 */
class EntityDetails(private val entity: Entity) : Widget {
	private val preview = MemoizedContent<Component>({ getWidget(Component::class.java, it) }, Widget { text("Select a component to preview") })
	private val details = WindowManifest<KClass<out Component>>()

	private var create: Modal? = null
	private var createContent: Widget? = null

	private var toAdd: Component? = null
	private var toRemove: Class<out Component>? = null

	override fun invoke() {
		list("##components") {
			entity.forEach {
				selectable(it::class.simpleName.toString(), ImGuiSelectableFlags.AllowDoubleClick) {
					// display inline on single click
					preview(it)

					onDoubleClick(GLFW.GLFW_MOUSE_BUTTON_1) {
						// in window on double click
						details[it::class] = { Window("Entity ${entity.id}: ${it::class.simpleName}", preview.value, minSize = componentMinSize) }
						preview.reset()
					}
				}
				contextMenu {
					drawAddMenu()
					menuItem("remove") {
						toRemove = it::class.java
					}
				}
			}

			if (entity.size() <= 0) {
				// draw a dummy row for contextual actions on empty lists
				selectable("##empty") {}
				contextMenu {
					drawAddMenu()
				}
			}
		}
		// augment elements only after done iterating
		toAdd?.let {
			entity.put(it)
			toAdd = null
		}
		toRemove?.let {
			entity.remove(it)
			preview.reset()
			toRemove = null
		}

		separator()
		preview.value()

		// open outside the menu creating the modal because ID stack
		createContent?.let {
			create?.open(it)
			createContent = null
		}
		create?.invoke()

		details()
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
