package dev.kkorolyov.pancake.skillet.ui.entity

import dev.kkorolyov.pancake.skillet.decorator.*
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.Model.ModelChangeEvent
import dev.kkorolyov.pancake.skillet.model.ModelListener
import dev.kkorolyov.pancake.skillet.model.Workspace
import dev.kkorolyov.pancake.skillet.model.Workspace.WorkspaceChangeEvent
import dev.kkorolyov.pancake.skillet.ui.Panel
import javafx.scene.control.Tab
import javafx.scene.control.TabPane

/**
 * Displays editable entity tabs.
 * @param entitySelected listener invoked with the selected entity when an entity is selected
 * @param entityClosed listener invoked with the closed entity when its editor is closed
 */
class EntityTabPane(
		var entitySelected: (GenericEntity?) -> Unit = {},
		var entityClosed: (GenericEntity) -> Unit = {}
) : Panel, ModelListener<Workspace> {
	private val tabs: MutableMap<GenericEntity, Tab> = HashMap()

	override val root: TabPane = TabPane()
			.styleClass("entity-tabs")

	/**
	 * Adds a new entity tab if it does not yet exist and selects it.
	 * @param entity associated entity
	 */
	fun add(entity: GenericEntity) {
		root.selectionModel.select(tabs.computeIfAbsent(entity) {
			val tab = Tab()
					.styleClass("entity-tab")
					.graphic(EntityLabel(it).root)
					.content(EntityPanel(it).root)
					.change(Tab::selectedProperty) { _, _, newValue ->
						if (newValue) entitySelected(entity)
					}
					.close { entityClosed(entity) }

			root.tabs += tab

			tab
		})
	}

	private fun removeOldEntities(workspace: Workspace) {
		tabs
				.filter { !workspace.containsEntity(it.key) }
				.forEach {
					tabs -= it.key
					root.tabs -= it.value
				}
		if (tabs.isEmpty()) entitySelected(null)
	}

	override fun changed(target: Workspace, event: ModelChangeEvent) {
		when(event) {
			// TODO Replace this Optional
			WorkspaceChangeEvent.ACTIVE -> { target.activeEntity.ifPresent(this::add) }
			WorkspaceChangeEvent.REMOVE -> { removeOldEntities(target) }
		}
	}
}
