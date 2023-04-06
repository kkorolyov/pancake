package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.MemoizedContent
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.menuItem
import dev.kkorolyov.pancake.editor.onDoubleClick
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.separator
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
	private val preview = MemoizedContent(::EntityDetails, Widget { text("Select an entity to preview") })
	private val details = WindowManifest<Int>()

	private var toAdd = false
	private var toRemove: Int? = null

	override fun invoke() {
		table("entities", 2, ImGuiTableFlags.SizingStretchProp) {
			ImGui.tableSetupColumn("ID")
			ImGui.tableSetupColumn("Components")
			ImGui.tableSetupScrollFreeze(1, 1)
			ImGui.tableHeadersRow()

			var empty = true
			entities.forEach {
				empty = false
				column {
					selectable(it.id.toString(), ImGuiSelectableFlags.SpanAllColumns or ImGuiSelectableFlags.AllowDoubleClick) {
						preview(it)

						onDoubleClick(GLFW_MOUSE_BUTTON_1) {
							details[it.id] = { Window("Entity ${it.id}", preview.value, minSize = entityMinSize) }
							preview.reset()
						}
					}
					contextMenu {
						drawAddMenu()
						menuItem("destroy") {
							toRemove = it.id
						}
					}
				}
				column {
					text(it.size())
				}
			}

			if (empty) {
				// draw a dummy row for contextual actions on empty tables
				column {
					selectable("##empty", ImGuiSelectableFlags.SpanAllColumns) {}
					contextMenu {
						drawAddMenu()
					}
				}
			}
		}
		// augment elements only after done iterating
		if (toAdd) {
			preview(entities.create())
			toAdd = false
		}
		toRemove?.let {
			entities.destroy(it)
			preview.reset()
			toRemove = null
		}

		separator()
		preview.value()

		details()
	}

	private fun drawAddMenu() {
		menuItem("new") {
			toAdd = true
		}
	}
}
