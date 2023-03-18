package dev.kkorolyov.pancake.editor.factory

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.onDoubleClick
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.editor.widget.Window
import dev.kkorolyov.pancake.editor.widget.WindowManifest
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
 * Provides widgets drawing entities.
 */
object EntityWidgetFactory : WidgetFactory<Entity> {
	/**
	 * Returns a widget displaying [t].
	 */
	override fun get(t: Entity): Widget {
		var detail = Widget { text("select a component to preview") }
		val details = WindowManifest<KClass<out Component>>()

		var create = Widget { text("select a component type to add") }

		return Widget {
			table("${t.id}.content", 2) {
				column {
					text("Current")
					tooltip("click to preview, double-click to open in new window")
					list("##components") {
						t.forEach {
							selectable(it::class.simpleName.toString(), ImGuiSelectableFlags.AllowDoubleClick) {
								// display inline on single click
								detail = getComponentWidget(it)

								onDoubleClick(GLFW.GLFW_MOUSE_BUTTON_1) {
									// in window on double click
									details[it::class] = { Window("Entity ${t.id}: ${it::class.simpleName}", detail, minSize = componentMinSize) }
								}
							}
						}
					}
				}
				column {
					text("Add new")
					list("##newComponents") {
						componentTypes.forEach { type ->
							selectable(type.simpleName) {
								create = getComponentWidget(type, t::put)
							}
						}
					}
				}

				column {
					separator()
					detail()
				}
				column {
					separator()
					create()
				}
			}

			details()
		}
	}

	/**
	 * Returns a widget creating entities of type [c], invoking [onNew] with the created entity.
	 */
	override fun get(c: Class<Entity>, onNew: (Entity) -> Unit): Widget {
		TODO("Not yet implemented")
	}
}
