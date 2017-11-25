package dev.kkorolyov.pancake.skillet.ui.component

import dev.kkorolyov.pancake.skillet.decorator.action
import dev.kkorolyov.pancake.skillet.decorator.compact
import dev.kkorolyov.pancake.skillet.decorator.maxSize
import dev.kkorolyov.pancake.skillet.decorator.styleClass
import dev.kkorolyov.pancake.skillet.model.GenericComponent
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.GenericEntity.EntityChangeEvent
import dev.kkorolyov.pancake.skillet.model.ModelListener
import dev.kkorolyov.pancake.skillet.model.factory.ComponentFactory
import dev.kkorolyov.pancake.skillet.model.factory.ComponentFactory.ComponentFactoryChangeEvent
import dev.kkorolyov.pancake.skillet.ui.Panel
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox

/**
 * Displays a list of components which may be added to the designed entity.
 * @param componentFactory displayed component factory
 * @param componentSelected listener invoked with the selected component when a component is selected
 */
class ComponentList(
		componentFactory: ComponentFactory,
		var componentSelected: (GenericComponent) -> Unit = {}
) : Panel {
	private var lastKnown: GenericEntity? = null
	private val lastKnownListener: ModelListener<GenericEntity> = {target, event ->
		when (event) {
			EntityChangeEvent.ADD, EntityChangeEvent.REMOVE -> refreshComponents(target)
		}
	}
	private val buttons: MutableMap<String, Button> = HashMap()

	private val content: VBox = VBox()
	override val root: VBox = VBox(
			Label("Components")
					.styleClass("panel-header"),
			ScrollPane(content)
					.compact())

	init {
		componentFactory.register({ target, event ->
			when (event) {
				ComponentFactoryChangeEvent.ADD -> { addNewComponents(target) }
				ComponentFactoryChangeEvent.REMOVE -> { removeOldComponents(target) }
			}
		})
	}

	/**
	 * Disables buttons for all components present in an entity, enables buttons for all others.
	 * @param entity entity according to which to disable/enable component buttons
	 */
	fun refreshComponents(entity: GenericEntity?) {
		if (lastKnown !== null && lastKnown !== entity) lastKnown?.unregister(lastKnownListener)
		lastKnown = entity
		lastKnown?.register(lastKnownListener)

		buttons.forEach { name, button -> button.isDisable = entity?.contains(name) ?: true }
	}

	private fun addNewComponents(componentFactory: ComponentFactory) {
		componentFactory.names.forEach {
			buttons.computeIfAbsent(it) {
				val button = Button(it)
						.maxSize(Double.MAX_VALUE)
						.apply {
							action {
								val component = componentFactory[it]!!	// Should never be null
								lastKnown?.plusAssign(component)
								componentSelected(component)
							}
						}
				content.children += button
				button
			}
		}
		refreshComponents(lastKnown)
	}
	private fun removeOldComponents(componentFactory: ComponentFactory) {
		buttons.filter { it.key !in componentFactory }
				.forEach { name, button ->
					buttons -= name
					content.children -= button
				}
	}
}
