package dev.kkorolyov.pancake.skillet.ui.entity

import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.Workspace
import dev.kkorolyov.pancake.skillet.model.Workspace.WorkspaceChangeEvent
import dev.kkorolyov.pancake.skillet.ui.Panel
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import java.util.IdentityHashMap

/**
 * Displays editable entity tabs.
 * @param workspace workspace containing editable entities
 * @param entitySelected listener invoked with the selected entity when an entity is selected
 * @param entityClosed listener invoked with the closed entity when its editor is closed
 */
class EntityTabPane(
		private val workspace: Workspace,
		var entitySelected: (GenericEntity?) -> Unit = {},
		var entityClosed: (GenericEntity) -> Unit = {}
) : Panel {
	private val tabs: MutableMap<GenericEntity, Tab> = IdentityHashMap()

	override val root: TabPane = TabPane().apply {
		styleClass += "entity-tabs"
	}

	init {
		workspace.register({ target, event ->
			when (event) {
				WorkspaceChangeEvent.ACTIVE -> { target.activeEntity?.let(this::add) }
				WorkspaceChangeEvent.REMOVE -> { removeOldEntities(target) }
			}
		})
	}

	/**
	 * Adds a new entity tab if it does not yet exist and selects it.
	 * @param entity associated entity
	 */
	fun add(entity: GenericEntity) {
		val tab = tabs.computeIfAbsent(entity) {
			Tab().apply {
				styleClass += "entity-tab"
				graphic = EntityLabel(entity).root
				content = EntityPanel(entity).root
				selectedProperty().addListener { _, _, newValue ->
					if (newValue) {
						workspace.activeEntity = entity
						entitySelected(entity)
					}
				}
				setOnClosed {
					workspace.activeEntity = null
					entityClosed(entity)
				}
				root.tabs += this
			}
		}
		if (tab !in root.tabs) root.tabs += tab
		root.selectionModel.select(tab)
	}

	private fun removeOldEntities(workspace: Workspace) {
		tabs.filter { it.key !in workspace }
				.forEach { entity, tab ->
					tabs -= entity
					root.tabs -= tab
				}
		if (tabs.isEmpty()) entitySelected(null)
	}
}
