package dev.kkorolyov.pancake.skillet.ui.entity

import dev.kkorolyov.pancake.skillet.decorator.action
import dev.kkorolyov.pancake.skillet.decorator.compact
import dev.kkorolyov.pancake.skillet.decorator.contextMenu
import dev.kkorolyov.pancake.skillet.decorator.item
import dev.kkorolyov.pancake.skillet.decorator.maxSize
import dev.kkorolyov.pancake.skillet.decorator.styleClass
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.GenericEntity.EntityChangeEvent
import dev.kkorolyov.pancake.skillet.model.Workspace
import dev.kkorolyov.pancake.skillet.model.Workspace.WorkspaceChangeEvent
import dev.kkorolyov.pancake.skillet.ui.Panel
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

	private val content: VBox = VBox()
	override val root: VBox = VBox(
			Label("Entities")
					.styleClass("panel-header"),
			ScrollPane(content)
					.compact())

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
			buttons.computeIfAbsent(it) {
				val button = Button(it.name)
						.maxSize(Double.MAX_VALUE)
						.action { workspace.activeEntity = it }
						.contextMenu {
							ContextMenu()
									.item("Remove") { workspace.removeEntity(it) }
						}
				it.register({ target, event ->
					when (event) {
						EntityChangeEvent.NAME -> button.text = target.name
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
}
