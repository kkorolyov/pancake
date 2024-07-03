package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Clipper
import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.FileAccess
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.group
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.onDrag
import dev.kkorolyov.pancake.editor.sameLine
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.toStructEntity
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.EntityTemplate
import dev.kkorolyov.pancake.platform.io.BasicParsers
import dev.kkorolyov.pancake.platform.io.Resources
import dev.kkorolyov.pancake.platform.registry.Registry
import dev.kkorolyov.pancake.platform.registry.ResourceConverters
import imgui.ImGui
import imgui.flag.ImGuiHoveredFlags
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

	private val current = DebouncedValue<Entity, Widget> { EntityDetails(it) }

	private val inlineDetails = Popup("inlineDetails")
	private val errorMsg = Modal("ERROR")

	private var focusedEntity: Int? = null
	private var toAdd = false
	private var toRemove: Entity? = null

	private lateinit var rowRenderer: Clipper<Entity>

	override fun invoke() {
		// leave room for controls below
		table("entities", 5, height = -Layout.lineHeight(1.5), flags = ImGuiTableFlags.ScrollY) {
			configColumn("ID")
			configColumn("Name")
			configColumn("Components")
			configColumn("Alias")
			configColumn("Select", ImGuiTableColumnFlags.WidthFixed)
			scrollFreeze(1, 1)
			headersRow()

			// initialize here to have a reference to the Table receiver
			if (!::rowRenderer.isInitialized) {
				rowRenderer = Clipper {
					column {
						selectable(it.id) {
							inlineDetails.open(current.set(it))
						}
						contextMenu {
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
						text(it.debugName)
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
			}

			rowRenderer(entities.toList())

			column {
				selectable("##empty", flags = ImGuiSelectableFlags.SpanAllColumns) { toAdd = true }
				tooltip("add entity")
			}

			inlineDetails()

			// augment elements only after done iterating
			if (toAdd) {
				focusedEntity = entities.create().id
				toAdd = false
			}
			toRemove?.let {
				selected.remove(it)
				entities.destroy(it.id)
				toRemove = null
			}
		}

		separator()
		group {
			button("import") {
				try {
					import()
				} catch (e: Exception) {
					errorMsg.open(Widget { text(e.message ?: e) })
					log.error("import error", e)
				}
			}
			tooltip("import entities")

			sameLine()

			disabledIf(selected.isEmpty()) {
				button("export") {
					try {
						export()
					} catch (e: Exception) {
						errorMsg.open(Widget { text(e.message ?: e) })
						log.error("export error", e)
					}
				}
				tooltip(
					ImGuiHoveredFlags.AllowWhenDisabled,
					"export ${if (selected.isEmpty()) "selected entities" else selected.joinToString { aliases[it] ?: it.id.toString() }}"
				)
			}
		}

		errorMsg()
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
					val entity = entities.create(alias)
					template.apply(entity)
					aliases[entity] = alias
				}
		} ?: throw IllegalArgumentException("failed to open file [$path]")
	}

	private fun export() {
		FileAccess.pickSave(FileAccess.Filter("YAML", "yaml", "yml"))?.let {
			if (it.endsWith(".yaml") || it.endsWith(".yml")) {
				exportYaml(it)
			} else {
				throw IllegalArgumentException("unknown file type [$it]")
			}
		}
	}

	private fun exportYaml(path: String) {
		Resources.outStream(path)?.use {
			Yaml(DumperOptions().apply { width = 1000 }).dump(
				selected.associate { entity -> (aliases[entity] ?: entity.id) to toStructEntity(entity) },
				OutputStreamWriter(it)
			)
		} ?: throw IllegalArgumentException("failed to open file [$path]")
	}
}
