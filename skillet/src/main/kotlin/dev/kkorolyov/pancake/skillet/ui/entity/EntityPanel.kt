package dev.kkorolyov.pancake.skillet.ui.entity

import dev.kkorolyov.pancake.skillet.decorator.compact
import dev.kkorolyov.pancake.skillet.decorator.styleClass
import dev.kkorolyov.pancake.skillet.model.GenericComponent
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.GenericEntity.EntityChangeEvent
import dev.kkorolyov.pancake.skillet.ui.Panel
import dev.kkorolyov.pancake.skillet.ui.component.ComponentPanel
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox

/**
 * Displays a [GenericEntity].
 * @param entity displayed entity
 */
class EntityPanel(entity: GenericEntity) : Panel {
	private val panels: MutableMap<GenericComponent, ComponentPanel> = HashMap()

	private val content: VBox = VBox()
			.styleClass("entity-content")
	override val root: ScrollPane = ScrollPane(content)
			.compact()

	init {
		entity.register({ target, event ->
			when (event) {
				EntityChangeEvent.ADD -> { addNewComponents(target) }
				EntityChangeEvent.REMOVE -> { removeOldComponents(target) }
			}
		})
		addNewComponents(entity)
	}

	private fun addNewComponents(entity: GenericEntity) {
		entity.components.forEach {
			panels.computeIfAbsent(it) {
				val component = ComponentPanel(it, entity)
				content.children += component.root

				component
			}
		}
	}
	private fun removeOldComponents(entity: GenericEntity) {
		panels.filter { it.key.name !in entity }
				.forEach { component, panel ->
					panels -= component
					content.children -= panel.root
				}
	}
}
