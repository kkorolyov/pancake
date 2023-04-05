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
import imgui.type.ImBoolean
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

private val noopModal = Modal("noop", Widget {}, ImBoolean(false))

/**
 * Displays and provides for modification of [entity] properties.
 */
class EntityDetails(private val entity: Entity) : Widget {
	private val preview = MemoizedContent<Component>({ getWidget(Component::class.java, it) }, Widget { text("Select a component to preview") })
	private val details = WindowManifest<KClass<out Component>>()

	private var create: Modal = noopModal

	private var toAdd: Component? = null
	private var toRemove: Class<out Component>? = null

	override fun invoke() {
		list("##components") {
			contextMenu(true) {
				drawAddMenu()
			}

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

		create()

		details()
	}

	private fun drawAddMenu() {
		menu("add") {
			componentTypes.forEach { type ->
				if (entity[type] == null) {
					menuItem(type.simpleName) {
						create = Modal(
							"New ${type.simpleName}",
							getWidget(Component::class.java, type) {
								create.visible = false
								toAdd = it
							},
							minSize = componentMinSize
						)
					}
				}
			}
		}
	}
}
