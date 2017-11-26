package dev.kkorolyov.pancake.skillet.ui.entity

import dev.kkorolyov.pancake.skillet.compact
import dev.kkorolyov.pancake.skillet.contextMenu
import dev.kkorolyov.pancake.skillet.item
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.GenericEntity.EntityChangeEvent
import dev.kkorolyov.pancake.skillet.model.Workspace
import dev.kkorolyov.pancake.skillet.model.Workspace.WorkspaceChangeEvent
import dev.kkorolyov.pancake.skillet.ui.Panel
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox
import java.util.IdentityHashMap

/**
 * Displays entities available in the [Workspace].
 * @param workspace workspace containing displayed entities
 */
class EntityList(workspace: Workspace) : Panel {
	private val buttons: MutableMap<GenericEntity, Button> = IdentityHashMap()

	private val content: VBox = VBox().apply {
		styleClass += "panel-content"
	}
	override val root: VBox = VBox().apply {
		styleClass += "panel"
		children += listOf(
				Label("Entities").apply {
					styleClass += "panel-header"
					maxWidth = Double.MAX_VALUE
					alignment = Pos.CENTER
				},
				ScrollPane(content).apply {
					compact()
				})
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
		workspace.entities.forEach {
			buttons.computeIfAbsent(it) { entity ->
				Button(it.name).apply {
					maxWidth = Double.MAX_VALUE
					setOnAction { workspace.activeEntity = entity }
					contextMenu {
						ContextMenu().apply {
							item("Remove") { workspace.removeEntity(entity) }
						}
					}
					entity.register({ target, event ->
						when (event) {
							EntityChangeEvent.NAME -> text = target.name
						}
					})
					content.children += this
				}
			}
		}
	}
	private fun removeOldEntities(workspace: Workspace) {
		buttons.filter { it.key !in workspace }
				.forEach { entity, button ->
					buttons -= entity
					content.children -= button
				}
	}
}
