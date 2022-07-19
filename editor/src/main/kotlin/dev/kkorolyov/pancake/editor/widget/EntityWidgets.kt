package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.getComponentWidget
import dev.kkorolyov.pancake.editor.indented
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import imgui.ImGui
import imgui.flag.ImGuiSelectableFlags
import kotlin.reflect.KClass

/**
 * Renders entity listings from [entities].
 */
class EntitiesTable(private val entities: EntityPool) : Widget {
	private val details = WindowManifest<Int>()

	override fun invoke() {
		table("entities", 2) {
			ImGui.tableSetupColumn("ID")
			ImGui.tableSetupColumn("Components")
			ImGui.tableSetupScrollFreeze(1, 1)
			ImGui.tableHeadersRow()

			entities.forEach {
				column {
					if (ImGui.selectable(it.id.toString(), false, ImGuiSelectableFlags.SpanAllColumns)) {
						details[it.id] = { Window("Entity ${it.id}", EntityDetails(it)) }
					}
				}
				column {
					ImGui.text(it.count().toString())
				}
			}
		}

		details()
	}
}

/**
 * Renders detailed entity information for [value].
 */
class EntityDetails(private val value: Entity) : Widget {
	private val details = WindowManifest<KClass<out Component>>()

	override fun invoke() {
		ImGui.text("Components")
		indented {
			list("##components") {
				value.forEach {
					if (ImGui.selectable(it::class.simpleName.toString())) {
						details[it::class] = { Window("${value.id}: ${it::class.simpleName}", getComponentWidget(it)) }
					}
				}
			}
		}

		details()
	}
}
