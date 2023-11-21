package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.FileAccess
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.child
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.menuItem
import dev.kkorolyov.pancake.editor.onDrag
import dev.kkorolyov.pancake.editor.onDrop
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.setDragDropPayload
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.editor.useDragDropPayload
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.EntityTemplate
import dev.kkorolyov.pancake.platform.io.BasicParsers
import dev.kkorolyov.pancake.platform.io.Resources
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.ImGui
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableColumnFlags
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.OutputStreamWriter

private val entityMinSize = Vector2.of(200.0, 200.0)

private val log = LoggerFactory.getLogger(EntitiesTable::class.java)

/**
 * Renders entity listings from [entities].
 * Submits expanded entity windows by entity to [entityManifest] and component windows by component to [componentManifest], which are both expected to be rendered externally.
 * This is to avoid feedback loops with docking dependent windows together.
 */
class EntitiesTable(private val entities: EntityPool, private val entityManifest: WindowManifest<Entity>, private val componentManifest: WindowManifest<Component>) : Widget {
	private val aliases = mutableMapOf<Entity, String>()
	private val selected = mutableSetOf<Entity>()

	private var exportPath = ""

	private val current = DebouncedValue<Entity, Widget> { EntityDetails(it, componentManifest) }

	private val inlineDetails = Popup("inlineDetails")
	private val errorMsg = Modal("ERROR")

	private var focusedEntity: Int? = null

	override fun invoke() {
		// leave room for controls below
		child("entities", height = ImGui.getContentRegionAvailY() - ImGui.getTextLineHeightWithSpacing() * 3) {
			table("entities", 4) {
				ImGui.tableSetupColumn("ID")
				ImGui.tableSetupColumn("Components")
				ImGui.tableSetupColumn("Alias")
				ImGui.tableSetupColumn("Select", ImGuiTableColumnFlags.WidthFixed)
				ImGui.tableSetupScrollFreeze(1, 1)
				ImGui.tableHeadersRow()

				var toAdd = false
				var toRemove: Entity? = null

				entities.forEach {
					column {
						selectable(it.id) {
							inlineDetails.open(current.set(it))
						}
						contextMenu {
							drawAddMenu { toAdd = true }
							menuItem("destroy") { toRemove = it }
						}
						onDrag {
							setDragDropPayload(it)
							current.set(it)()
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
		}
		onDrop {
			useDragDropPayload<Entity> {
				entityManifest[it] = { Window("Entity ${it.id}", EntityDetails(it, componentManifest), minSize = entityMinSize, openAt = OpenAt.Cursor) }
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
			BasicParsers.yaml()
				.andThen { EntityTemplate.read(it as Map<String, Any>) }
				.parse(it)
				.forEach { (alias, template) ->
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
