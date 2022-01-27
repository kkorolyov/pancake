package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.Mass
import dev.kkorolyov.pancake.editor.ComponentData
import dev.kkorolyov.pancake.editor.ComponentDataFactory
import dev.kkorolyov.pancake.editor.ComponentDetails
import dev.kkorolyov.pancake.platform.entity.Component
import javafx.beans.value.ObservableDoubleValue
import tornadofx.doubleProperty
import tornadofx.field
import tornadofx.fieldset
import tornadofx.form
import tornadofx.label

class MassComponentDataFactory : ComponentDataFactory {
	override fun getData(component: Component): ComponentData<*, *>? = (component as? Mass)?.let(::MassComponentData)

	override fun getDetails(data: ComponentData<*, *>): ComponentDetails? =
		(data as? MassComponentData)?.let(::MassComponentDetails)

	private data class MassComponentData(private val component: Mass) :
		ComponentData<Mass, MassComponentData>(component) {
		val value: ObservableDoubleValue
			get() = valueProperty

		private val valueProperty = doubleProperty()

		override fun refresh() {
			valueProperty.set(component.value)
		}

		override fun bind(other: MassComponentData?) {
			super.bind(other)
			other?.valueProperty?.let(valueProperty::bind) ?: valueProperty.unbind()
		}
	}

	private class MassComponentDetails(model: MassComponentData) : ComponentDetails() {
		override val root = label(model.value)
	}
}
