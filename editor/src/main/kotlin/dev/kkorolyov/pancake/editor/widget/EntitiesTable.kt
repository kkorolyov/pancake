package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.factory.EntityWidgetFactory
import dev.kkorolyov.pancake.editor.onDoubleClick
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.ImGui
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableFlags
import org.lwjgl.glfw.GLFW.*

private val entityMinSize = Vector2.of(200.0, 200.0)

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
							details[it.id] = { Window("Entity ${it.id}", EntityWidgetFactory.get(it), minSize = entityMinSize) }
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
