package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.factory.getComponentWidget
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.onDoubleClick
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
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
			.map { info ->
				info.loadClass(Component::class.java)
			}
	}
}

/**
 * Displays and provides for modification of [entity] properties.
 */
class EntityDetails(private val entity: Entity) : Widget {
	private val preview = MemoizedContent(::getComponentWidget, Widget { text("Select a component to preview") })
	private val details = WindowManifest<KClass<out Component>>()

	private var create = Widget { text("Select a component type to add") }

	private var toRemove: Class<out Component>? = null

	override fun invoke() {
		table("${entity.id}.content", 2) {
			column {
				text("Current")
				tooltip("click to preview, double-click to open in new window")
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
							selectable("remove") {
								toRemove = it::class.java
							}
						}
					}
				}
				toRemove?.let {
					entity.remove(it)
					toRemove = null
				}
			}
			column {
				text("Add new")
				list("##newComponents") {
					componentTypes.forEach { type ->
						selectable(type.simpleName) {
							create = getComponentWidget(type, entity::put)
						}
					}
				}
			}

			column {
				separator()
				preview.value()
			}
			column {
				separator()
				create()
			}
		}

		details()
	}
}
