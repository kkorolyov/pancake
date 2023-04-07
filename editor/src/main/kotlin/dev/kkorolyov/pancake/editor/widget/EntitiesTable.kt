package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.MemoizedContent
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.menuItem
import dev.kkorolyov.pancake.editor.onDoubleClick
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.EntityTemplate
import dev.kkorolyov.pancake.platform.io.Resources
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.ImGui
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableColumnFlags
import imgui.flag.ImGuiTableFlags
import org.lwjgl.glfw.GLFW.*
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.OutputStreamWriter

private val entityMinSize = Vector2.of(200.0, 200.0)

private val log = LoggerFactory.getLogger(EntitiesTable::class.java)

/**
 * Renders entity listings from [entities].
 */
class EntitiesTable(private val entities: EntityPool) : Widget {
	private val aliases = mutableMapOf<Entity, String>()
	private val selected = mutableSetOf<Entity>()

	private var exportPath = ""

	private val preview = MemoizedContent(::EntityDetails, Widget { text("Select an entity to preview") })
	private val details = WindowManifest<Int>()

	private var error: Modal? = null

	override fun invoke() {
		table("entities", 4) {
			ImGui.tableSetupColumn("ID")
			ImGui.tableSetupColumn("Components")
			ImGui.tableSetupColumn("Alias")
			ImGui.tableSetupColumn("Select", ImGuiTableColumnFlags.WidthFixed)
			ImGui.tableSetupScrollFreeze(1, 1)
			ImGui.tableHeadersRow()

			var empty = true
			var toAdd = false
			var toRemove: Entity? = null

			entities.forEach {
				empty = false

				column {
					selectable(it.id.toString(), ImGuiSelectableFlags.AllowDoubleClick) {
						preview(it)

						onDoubleClick(GLFW_MOUSE_BUTTON_1) {
							details[it.id] = { Window("Entity ${it.id}", preview.value, minSize = entityMinSize) }
							preview.reset()
						}
					}
					contextMenu {
						drawAddMenu { toAdd = true }
						menuItem("destroy") { toRemove = it }
					}
				}

				column {
					text(it.size())
				}

				column {
					input("##alias${it.id}", aliases[it] ?: "") { value -> aliases[it] = value }
				}

				column {
					input("##select${it.id}", it in selected) { value ->
						if (value) selected.add(it) else selected.remove(it)
					}
				}
			}

			if (empty) {
				// draw a dummy row for contextual actions on empty tables
				column {
					selectable("##empty", ImGuiSelectableFlags.SpanAllColumns) {}
					contextMenu {
						drawAddMenu { toAdd = true }
					}
				}
			}

			// augment elements only after done iterating
			if (toAdd) preview(entities.create())
			toRemove?.let {
				selected.remove(it)
				entities.destroy(it.id)
				preview.reset()
			}
		}

		if (!selected.isEmpty()) {
			separator()

			text("Export ${selected.joinToString { aliases[it] ?: it.id.toString() }}")

			input("##exportPath", exportPath) { exportPath = it }
			tooltip("export path")

			disabledIf(exportPath.isEmpty()) {
				button("YAML") {
					try {
						exportYaml()
						exportPath = ""
					} catch (e: Exception) {
						error = Modal("ERROR", Widget { text(e.message) })
						log.error("export error", e)
					}
				}
			}
		}

		separator()
		preview.value()

		details()

		error?.invoke()
	}

	private inline fun drawAddMenu(onAdd: () -> Unit) {
		menuItem("new", onAdd)
	}

	private fun exportYaml() {
		val data = selected.map { (aliases[it] ?: it.id) to EntityTemplate.write(it) }.toMap()
		Resources.outStream(exportPath)?.use {
			Yaml(DumperOptions().apply { width = 1000 }).dump(data, OutputStreamWriter(it))
		}
	}
}
