package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.VelocityCap
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

class VelocityCapComponentDataFactory : ComponentDataFactory {
	override fun getData(component: Component): ComponentData<*, *>? =
		(component as? VelocityCap)?.let(::VelocityCapComponentData)

	override fun getDetails(data: ComponentData<*, *>): ComponentDetails? =
		(data as? VelocityCapComponentData)?.let(::VelocityCapComponentDetails)

	private data class VelocityCapComponentData(private val component: VelocityCap) :
		ComponentData<VelocityCap, VelocityCapComponentData>(component) {
		val value: ObservableStringValue
			get() = valueProperty

		private val valueProperty = stringProperty()

		override fun refresh() {
			valueProperty.set(component.value.prettyPrint())
		}

		override fun bind(other: VelocityCapComponentData?) {
			super.bind(other)
			other?.valueProperty?.let(valueProperty::bind) ?: valueProperty.unbind()
		}
	}

	private class VelocityCapComponentDetails(model: VelocityCapComponentData) : ComponentDetails() {
		override val root = label(model.value)
	}
}
