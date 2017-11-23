package dev.kkorolyov.pancake.skillet.ui.entity

import dev.kkorolyov.pancake.skillet.decorator.*
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.Model.ModelChangeEvent
import dev.kkorolyov.pancake.skillet.model.Model.ModelListener
import dev.kkorolyov.pancake.skillet.model.Workspace
import dev.kkorolyov.pancake.skillet.model.Workspace.WorkspaceChangeEvent
import dev.kkorolyov.pancake.skillet.ui.Panel
import javafx.scene.control.Tab
import javafx.scene.control.TabPane

/**
 * Displays editable entity tabs.
 * @param workspace workspace containing editable entities
 * @param entitySelected listener invoked with the selected entity when an entity is selected
 * @param entityClosed listener invoked with the closed entity when its editor is closed
 */
class EntityTabPane(
		workspace: Workspace,
		var entitySelected: (GenericEntity?) -> Unit = {},
		var entityClosed: (GenericEntity) -> Unit = {}
) : Panel, ModelListener<Workspace> {
	private val tabs: MutableMap<GenericEntity, Tab> = HashMap()

	override val root: TabPane = TabPane()
			.styleClass("entity-tabs")

	init {
		workspace.register(this)
	}

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
		tabs.filter { it.key !in workspace }
				.forEach { entity, tab ->
					tabs -= entity
					root.tabs -= tab
				}
		if (tabs.isEmpty()) entitySelected(null)
	}

	override fun changed(target: Workspace, event: ModelChangeEvent) {
		when(event) {
			// TODO Replace this Optional
			WorkspaceChangeEvent.ACTIVE -> { target.activeEntity?.let(this::add) }
			WorkspaceChangeEvent.REMOVE -> { removeOldEntities(target) }
		}
	}
}
