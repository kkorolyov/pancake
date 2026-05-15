package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Clipper
import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.FileAccess
import dev.kkorolyov.pancake.editor.Key
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.onDrag
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.io.EntityPoolSerializer
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Resources
import dev.kkorolyov.pancake.platform.io.WriteContext
import imgui.ImGui
import imgui.flag.ImGuiKey
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableFlags
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

private val log = LoggerFactory.getLogger(EntitiesTable::class.java)

/**
 * Renders entity listings from [entities].
 * If [dragDropId] is provided, emits drag-drop payloads to it containing the selected [Entity].
 */
class EntitiesTable(private val entities: EntityPool, private val dragDropId: String? = null) : Widget {
	private val selected = mutableSetOf<Entity>()

	private val current = DebouncedValue<Entity, Widget> { EntityDetails(it) }

	private val inlineDetails = Popup("inlineDetails")
	private val errorMsg = Modal("ERROR")

	private var focusedEntity: Entity? = null
	private var toAdd = false
	private var toRemove = false
	private var toImport = false
	private var toExport = false

	private lateinit var rowRenderer: Clipper<Entity>

	override fun invoke() {
		// leave room for controls below
		table("entities", 2, height = -Layout.lineHeight(1.5), flags = ImGuiTableFlags.ScrollY or ImGuiTableFlags.SizingStretchProp or ImGuiTableFlags.Resizable) {
			configColumn("Entity")
			configColumn("Components")
			scrollFreeze(1, 1)
			headersRow()

			// initialize here to have a reference to the Table receiver
			if (!::rowRenderer.isInitialized) {
				rowRenderer = Clipper { entity, _ ->
					column {
						Layout.width(Layout.stretchWidth) {
							selectable(entity.debugName, entity in selected) {
								if (Key.onDown(ImGuiKey.ImGuiMod_Ctrl)) {
									if (entity !in selected) selected += entity else selected -= entity
								} else {
									selected.clear()
									inlineDetails.open(current.set(entity))
								}
							}
						}
						contextMenu {
							if (entity !in selected) {
								selected.clear()
								selected.add(entity)
							}
							menuItem("destroy ${selected.size} entities") { toRemove = true }
							menuItem("export ${selected.size} entities") { toExport = true }
						}

						dragDropId?.let { id ->
							onDrag {
								setDragDropPayload(entity, id)
								current.set(entity)()
							}
						}

						focusedEntity?.let { focused ->
							if (focused == entity) {
								ImGui.setScrollHereY()
								focusedEntity = null
							}
						}
					}

					column {
						text(entity.size())
					}
				}
			}

			rowRenderer(entities.toList())

			column {
				selectable("##empty", flags = ImGuiSelectableFlags.SpanAllColumns) { toAdd = true }
				tooltip("add entity")

				contextMenu {
					menuItem("import") { toImport = true }
				}
			}

			inlineDetails()
		}

		// augment elements only after done iterating
		if (toAdd) {
			focusedEntity = entities.create()
			toAdd = false
		}
		if (toRemove) {
			selected.forEach(Entity::destroy)
			selected.clear()
			toRemove = false
		}
		if (toImport) {
			trying("import error") {
				FileAccess.pickFile()?.let { path ->
					Resources.read(path).use { channel ->
						entities.create(EntityPoolSerializer().read(ReadContext(ByteBuffer.allocate(1 shl 10), channel)))
					}
				}
			}
			toImport = false
		}
		if (toExport) {
			trying("export error") {
				FileAccess.pickSave()?.let { path ->
					WriteContext(ByteBuffer.allocate(1024), Resources.write(path)).use { context ->
						EntityPoolSerializer().write(EntityPool().apply { create(selected) }, context)
					}
				}
			}
			toExport = false
		}

		errorMsg()
	}

	private inline fun trying(logErr: String, op: () -> Unit) {
		try {
			op()
		} catch (e: Exception) {
			errorMsg.open(Widget { text(e.message ?: e) })
			log.error(logErr, e)
		}
	}
}
