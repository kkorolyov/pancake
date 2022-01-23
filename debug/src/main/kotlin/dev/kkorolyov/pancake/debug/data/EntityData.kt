package dev.kkorolyov.pancake.debug.data

import dev.kkorolyov.pancake.debug.ComponentData
import dev.kkorolyov.pancake.debug.getComponentData
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import tornadofx.bind
import tornadofx.objectProperty
import tornadofx.observableListOf
import tornadofx.observableSetOf

data class EntityData(private val entity: Entity? = null) : DynamicModel<EntityData> {
	val id: ObservableValue<Int>
		get() = idProperty
	val components: ObservableList<ComponentData<*, *>> by lazy {
		observableListOf<ComponentData<*, *>>().apply {
			bind(componentSet, ::getComponentData)
		}
	}

	private val idProperty = objectProperty(entity?.id)
	private val componentSet = observableSetOf<Component>()

	init {
		refresh()
	}

	override fun refresh() {
		entity?.let {
			val currentComponents = it.toSet()
			componentSet.retainAll(currentComponents)
			componentSet.addAll(currentComponents)

			components.forEach(ComponentData<*, *>::refresh)
		}
	}

	override fun bind(other: EntityData?) {
		other?.idProperty?.let(idProperty::bind) ?: idProperty.unbind()
		other?.components?.let { components.bind(it) { it } }
	}
}
