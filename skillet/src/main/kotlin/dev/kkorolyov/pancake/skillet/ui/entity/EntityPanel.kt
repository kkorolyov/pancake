package dev.kkorolyov.pancake.skillet.ui.entity

import dev.kkorolyov.pancake.skillet.decorator.compact
import dev.kkorolyov.pancake.skillet.decorator.styleClass
import dev.kkorolyov.pancake.skillet.model.GenericComponent
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.GenericEntity.EntityChangeEvent
import dev.kkorolyov.pancake.skillet.model.Model.ModelChangeEvent
import dev.kkorolyov.pancake.skillet.model.ModelListener
import dev.kkorolyov.pancake.skillet.ui.Panel
import dev.kkorolyov.pancake.skillet.ui.component.ComponentPanel
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox

/**
 * Displays a [GenericEntity].
 */
class EntityPanel(entity: GenericEntity) : Panel, ModelListener<GenericEntity> {
	private val components: MutableMap<GenericComponent, ComponentPanel> = HashMap()

	private val content: VBox = VBox()
	override val root: ScrollPane = ScrollPane(content)
			.styleClass("entity-content")
			.compact()

	init {
		entity.register(this)
	}

	private fun addNewComponents(entity: GenericEntity) {
		entity.components.forEach {
			components.computeIfAbsent(it) {
				val component = ComponentPanel(it)
						.onComponentRemoved { entity.removeComponent(it.name) }

				content.children += component.root

				component
			}
		}
	}
	private fun removeOldComponents(entity: GenericEntity) {
		components
				.filter { !entity.containsComponent(it.key.name) }
				.forEach {
					entity.removeComponent(it.key.name)
					content.children -= it.value.root
				}
	}

	override fun changed(target: GenericEntity, event: ModelChangeEvent) {
		when(event) {
			EntityChangeEvent.ADD -> { addNewComponents(target) }
			EntityChangeEvent.REMOVE -> { removeOldComponents(target) }
		}
	}
}
