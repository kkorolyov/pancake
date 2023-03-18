package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.factory.getComponentWidget
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.onDoubleClick
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.ImGui
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableFlags
import io.github.classgraph.ClassGraph
import org.lwjgl.glfw.GLFW.*
import kotlin.reflect.KClass

private val entityMinSize = Vector2.of(200.0, 200.0)
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
 * Renders entity listings from [entities].
 */
class EntitiesTable(private val entities: EntityPool) : Widget {
	private val details = WindowManifest<Int>()

	override fun invoke() {
		table("entities", 2, ImGuiTableFlags.SizingStretchProp) {
			ImGui.tableSetupColumn("ID")
			ImGui.tableSetupColumn("Components")
			ImGui.tableSetupScrollFreeze(1, 1)
			ImGui.tableHeadersRow()

			entities.forEach {
				column {
					selectable(it.id.toString(), ImGuiSelectableFlags.SpanAllColumns or ImGuiSelectableFlags.AllowDoubleClick) {
						onDoubleClick(GLFW_MOUSE_BUTTON_1) {
							details[it.id] = { Window("Entity ${it.id}", EntityDetails(it), minSize = entityMinSize) }
						}
					}
				}
				column {
					text(it.size())
				}
			}
		}

		details()
	}
}

/**
 * Renders detailed information for [entity].
 */
class EntityDetails(private val entity: Entity) : Widget {
	private var detail = Widget { text("select a component to preview") }
	private val details = WindowManifest<KClass<out Component>>()

	private var create = Widget { text("select a component type to add") }

	override fun invoke() {
		table("${entity.id}.content", 2) {
			column {
				text("Current")
				tooltip("click to preview, double-click to open in new window")
				list("##components") {
					entity.forEach {
						selectable(it::class.simpleName.toString(), ImGuiSelectableFlags.AllowDoubleClick) {
							// display inline on single click
							detail = getComponentWidget(it)

							onDoubleClick(GLFW_MOUSE_BUTTON_1) {
								// in window on double click
								details[it::class] = { Window("Entity ${entity.id}: ${it::class.simpleName}", detail, minSize = componentMinSize) }
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
							create = getComponentWidget(type, entity::put)
						}
					}
				}
			}

			column { detail() }
			column { create() }
		}

		details()
	}
}
