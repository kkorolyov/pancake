package dev.kkorolyov.pancake.skillet.ui.entity

import dev.kkorolyov.pancake.skillet.decorator.*
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.GenericEntity.EntityChangeEvent
import dev.kkorolyov.pancake.skillet.model.Model.ModelChangeEvent
import dev.kkorolyov.pancake.skillet.model.Model.ModelListener
import dev.kkorolyov.pancake.skillet.model.Workspace
import dev.kkorolyov.pancake.skillet.model.Workspace.WorkspaceChangeEvent
import dev.kkorolyov.pancake.skillet.ui.Panel
import javafx.scene.control.Button
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox

/**
 * Displays entities available in the [Workspace].
 * @param workspace workspace containing displayed entities
 */
class EntityList(workspace: Workspace) : Panel, ModelListener<Workspace> {
	private val buttons: MutableMap<GenericEntity, Button> = HashMap()

	private val content: VBox = VBox()
	override val root: VBox = VBox(
			Label("Entities")
					.styleClass("panel-header"),
			ScrollPane(content)
					.compact())

	init {
		workspace.register(this)
		addNewEntities(workspace)
	}

	private fun addNewEntities(workspace: Workspace) {
		workspace.entities.forEach {
			buttons.computeIfAbsent(it) {
				val button = Button(it.name)
						.maxSize(Double.MAX_VALUE)
						.action { workspace.activeEntity = it }
						.contextMenu {
							ContextMenu()
									.item("Remove") { workspace.removeEntity(it) }
						}
				it.register(object : ModelListener<GenericEntity> {
					override fun changed(target: GenericEntity, event: ModelChangeEvent) {
						if (EntityChangeEvent.NAME == event) button.text = target.name
					}
				})
				content.children += button

				button
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

	override fun changed(target: Workspace, event: ModelChangeEvent) {
		when(event) {
			WorkspaceChangeEvent.ADD -> { addNewEntities(target) }
			WorkspaceChangeEvent.REMOVE -> { removeOldEntities(target) }
		}
	}
}
