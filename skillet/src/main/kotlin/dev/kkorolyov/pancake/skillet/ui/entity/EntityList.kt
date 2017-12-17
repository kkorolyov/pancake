package dev.kkorolyov.pancake.skillet.ui.entity

import dev.kkorolyov.pancake.skillet.contextMenu
import dev.kkorolyov.pancake.skillet.item
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.GenericEntity.EntityChangeEvent
import dev.kkorolyov.pancake.skillet.model.Workspace
import dev.kkorolyov.pancake.skillet.model.Workspace.WorkspaceChangeEvent
import dev.kkorolyov.pancake.skillet.ui.Panel
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.cell.TextFieldListCell
import javafx.scene.layout.VBox
import javafx.util.Callback
import javafx.util.StringConverter

/**
 * Displays entities available in the [Workspace].
 * @param workspace workspace containing displayed entities
 */
class EntityList(workspace: Workspace) : Panel {
	private val content: ListView<GenericEntity> = ListView<GenericEntity>().apply {
		styleClass += "panel-content"
		isEditable = true
		cellFactory = Callback {
			object : TextFieldListCell<GenericEntity>() {
				override fun updateItem(item: GenericEntity?, empty: Boolean) {
					super.updateItem(item, empty)
					item?.let { entity ->
						text = entity.name

						contextMenu {
							ContextMenu().apply {
								item("Remove") { workspace.removeEntity(entity) }
							}
						}
					}
				}
			}.apply {
				converter = object : StringConverter<GenericEntity>() {
					override fun toString(o: GenericEntity): String = o.name
					override fun fromString(s: String): GenericEntity = item.apply { name = s }
				}
				prefWidthProperty().bind(it.widthProperty().subtract(4))
			}
		}
		selectionModel.selectedItemProperty().addListener { _, _, newValue ->
			workspace.activeEntity = newValue
		}
	}
	override val root: VBox = VBox().apply {
		styleClass += "panel"
		children += listOf(
				Label("Entities").apply {
					styleClass += "panel-header"
				},
				content)
	}

	init {
		workspace.register({ target, event ->
			when (event) {
				WorkspaceChangeEvent.ADD -> { addNewEntities(target) }
				WorkspaceChangeEvent.REMOVE -> { removeOldEntities(target) }
			}
		})
		addNewEntities(workspace)
	}

	private fun addNewEntities(workspace: Workspace) {
		workspace.entities.filter { entity ->
			content.items.none { it === entity }
		}.forEach {
			it.register { _, event ->
				when (event) {
					EntityChangeEvent.NAME -> content.refresh()
				}
			}
			content.items.add(it)
		}
	}
	private fun removeOldEntities(workspace: Workspace) {
		content.items.filter { it !in workspace }
				.forEach { content.items.remove(it) }
	}
}
