package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.onDoubleClick
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
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
	private val preview = MemoizedContent(::EntityDetails, Widget { text("Select an entity to preview") })

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
						preview(it)

						onDoubleClick(GLFW_MOUSE_BUTTON_1) {
							details[it.id] = { Window("Entity ${it.id}", preview.value, minSize = entityMinSize) }
							preview.reset()
						}
					}
					contextMenu {
						selectable("destroy") {
							entities.destroy(it.id)
						}
					}
				}
				column {
					text(it.size())
				}
			}
		}

		button("+") {
			preview(entities.create())
		}
		tooltip("New entity")

		separator()
		preview.value()

		details()
	}
}