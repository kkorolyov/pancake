package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.Velocity
import dev.kkorolyov.pancake.editor.ComponentData
import dev.kkorolyov.pancake.editor.ComponentDataFactory
import dev.kkorolyov.pancake.editor.ComponentDetails
import dev.kkorolyov.pancake.editor.prettyPrint
import dev.kkorolyov.pancake.platform.entity.Component
import javafx.beans.value.ObservableStringValue
import tornadofx.field
import tornadofx.fieldset
import tornadofx.form
import tornadofx.label
import tornadofx.stringProperty

class VelocityComponentDataFactory : ComponentDataFactory {
	override fun getData(component: Component): ComponentData<*, *>? =
		(component as? Velocity)?.let(::VelocityComponentData)

	override fun getDetails(data: ComponentData<*, *>): ComponentDetails? =
		(data as? VelocityComponentData)?.let(::VelocityComponentDetails)

	private data class VelocityComponentData(private val component: Velocity) :
		ComponentData<Velocity, VelocityComponentData>(component) {
		val value: ObservableStringValue
			get() = valueProperty

		private val valueProperty = stringProperty()

		override fun refresh() {
			valueProperty.set(component.value.prettyPrint())
		}

		override fun bind(other: VelocityComponentData?) {
			super.bind(other)
			other?.valueProperty?.let(valueProperty::bind) ?: valueProperty.unbind()
		}
	}

	private class VelocityComponentDetails(model: VelocityComponentData) : ComponentDetails() {
		override val root = label(model.value)
	}
}
