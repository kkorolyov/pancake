package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.getComponentWidget
import dev.kkorolyov.pancake.editor.indented
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.ImGui
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableFlags
import kotlin.reflect.KClass

private const val ENTITY_MIN_WIDTH = 200.0
private const val ENTITY_MIN_HEIGHT = 200.0
private const val COMPONENT_MIN_WIDTH = 200.0
private const val COMPONENT_MIN_HEIGHT = 100.0

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
					if (ImGui.selectable(it.id.toString(), false, ImGuiSelectableFlags.SpanAllColumns)) {
						details[it.id] = { Window("Entity ${it.id}", EntityDetails(it), minSize = Vector2.of(ENTITY_MIN_WIDTH, ENTITY_MIN_HEIGHT)) }
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
	private val details = WindowManifest<KClass<out Component>>()

	override fun invoke() {
		text("Components")
		indented {
			list("##components") {
				entity.forEach {
					if (ImGui.selectable(it::class.simpleName.toString())) {
						details[it::class] = { Window("Entity ${entity.id}: ${it::class.simpleName}", getComponentWidget(it), minSize = Vector2.of(COMPONENT_MIN_WIDTH, COMPONENT_MIN_HEIGHT)) }
					}
				}
			}
		}

		details()
	}
}
