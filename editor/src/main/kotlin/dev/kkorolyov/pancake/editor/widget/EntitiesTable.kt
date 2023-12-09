package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Clipper
import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.FileAccess
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.menuItem
import dev.kkorolyov.pancake.editor.onDrag
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.setDragDropPayload
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.EntityTemplate
import dev.kkorolyov.pancake.platform.io.BasicParsers
import dev.kkorolyov.pancake.platform.io.Resources
import dev.kkorolyov.pancake.platform.registry.Registry
import dev.kkorolyov.pancake.platform.registry.ResourceConverters
import imgui.ImGui
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableColumnFlags
import imgui.flag.ImGuiTableFlags
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.OutputStreamWriter

private val log = LoggerFactory.getLogger(EntitiesTable::class.java)

/**
 * Renders entity listings from [entities].
 * If [dragDropId] is provided, emits drag-drop payloads to it containing the selected [Entity].
 */
class EntitiesTable(private val entities: EntityPool, private val dragDropId: String? = null) : Widget {
	private val aliases = mutableMapOf<Entity, String>()
	private val selected = mutableSetOf<Entity>()

	private var exportPath = ""

	private val current = DebouncedValue<Entity, Widget> { EntityDetails(it) }

	private val inlineDetails = Popup("inlineDetails")
	private val errorMsg = Modal("ERROR")

	private var focusedEntity: Int? = null
	private var toAdd = false
	private var toRemove: Entity? = null

	private val rowRenderer = Clipper<Entity> {
		column {
			selectable(it.id) {
				inlineDetails.open(current.set(it))
			}
			contextMenu {
				drawAddMenu { toAdd = true }
				menuItem("destroy") { toRemove = it }
			}

			dragDropId?.let { id ->
				onDrag {
					setDragDropPayload(it, id)
					current.set(it)()
				}
			}

			focusedEntity?.let { focusedId ->
				if (focusedId == it.id) {
					ImGui.setScrollHereY()
					focusedEntity = null
				}
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

	override fun invoke() {
		// leave room for controls below
		table("entities", 4, height = -(ImGui.getTextLineHeightWithSpacing() * 6), flags = ImGuiTableFlags.ScrollY) {
			ImGui.tableSetupColumn("ID")
			ImGui.tableSetupColumn("Components")
			ImGui.tableSetupColumn("Alias")
			ImGui.tableSetupColumn("Select", ImGuiTableColumnFlags.WidthFixed)
			ImGui.tableSetupScrollFreeze(1, 1)
			ImGui.tableHeadersRow()

			rowRenderer(entities.toList())

			column {
				selectable("##empty", ImGuiSelectableFlags.SpanAllColumns) {}
				contextMenu {
					drawAddMenu { toAdd = true }
				}
			}

			inlineDetails()

			// augment elements only after done iterating
			if (toAdd) focusedEntity = entities.create().id
			toRemove?.let {
				selected.remove(it)
				entities.destroy(it.id)
				toRemove = null
			}
		}

		separator()
		button("import") {
			import()
		}

		if (selected.isNotEmpty()) {
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
						errorMsg.open(Widget { text(e.message ?: e) })
						log.error("export error", e)
					}
				}
			}
		}

		errorMsg()
	}

	private inline fun drawAddMenu(onAdd: () -> Unit) {
		menuItem("new", onAdd)
	}

	private fun import() {
		FileAccess.pickFile(FileAccess.Filter("YAML", "yaml", "yml"))?.let {
			if (it.endsWith(".yaml") || it.endsWith(".yml")) {
				importYaml(it)
			} else {
				throw IllegalArgumentException("unknown file type [$it]")
			}
		}
	}

	private fun importYaml(path: String) {
		Resources.inStream(path)?.use {
			Registry<EntityTemplate>().apply {
				putAll(
					BasicParsers.yaml()
						.andThen(ResourceConverters.get(EntityTemplate::class.java))
						.parse(it)
				)
			}
				.forEach { alias, template ->
					val entity = entities.create()
					template.apply(entity)
					aliases[entity] = alias
				}
		} ?: throw IllegalArgumentException("failed to open file [$path]")
	}

	private fun exportYaml() {
		val data = selected.map { (aliases[it] ?: it.id) to EntityTemplate.write(it) }.toMap()
		Resources.outStream(exportPath)?.use {
			Yaml(DumperOptions().apply { width = 1000 }).dump(data, OutputStreamWriter(it))
		}
	}
}
