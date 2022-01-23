package dev.kkorolyov.pancake.debug

import dev.kkorolyov.pancake.debug.data.DynamicModel
import dev.kkorolyov.pancake.platform.entity.Component
import javafx.beans.value.ObservableValue
import tornadofx.objectProperty

open class ComponentData<T : Component, D : ComponentData<T, D>>(component: T? = null) : DynamicModel<D> {
	val name: ObservableValue<String>
		get() = nameProperty
	val stringValue: ObservableValue<String>
		get() = stringValueProperty

	private val nameProperty = objectProperty(if (component != null) component::class.simpleName else null)
	private val stringValueProperty = objectProperty(component.toString())

	override fun refresh() {}
	override fun bind(other: D?) {
		other?.nameProperty?.let(nameProperty::bind) ?: nameProperty.unbind()
		other?.stringValueProperty?.let(stringValueProperty::bind) ?: stringValueProperty.unbind()
	}
}
