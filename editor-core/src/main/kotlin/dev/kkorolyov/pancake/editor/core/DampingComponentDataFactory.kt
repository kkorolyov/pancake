package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.Damping
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

class DampingComponentDataFactory : ComponentDataFactory {
	override fun getData(component: Component): ComponentData<*, *>? =
		(component as? Damping)?.let(::DampingComponentData)

	override fun getDetails(data: ComponentData<*, *>): ComponentDetails? =
		(data as? DampingComponentData)?.let(::DampingComponentDetails)

	private data class DampingComponentData(private val component: Damping) :
		ComponentData<Damping, DampingComponentData>(component) {
		val value: ObservableStringValue
			get() = valueProperty

		private val valueProperty = stringProperty()

		override fun refresh() {
			valueProperty.set(component.value.prettyPrint())
		}

		override fun bind(other: DampingComponentData?) {
			super.bind(other)
			other?.valueProperty?.let(valueProperty::bind) ?: valueProperty.unbind()
		}
	}

	private class DampingComponentDetails(model: DampingComponentData) : ComponentDetails() {
		override val root = label(model.value)
	}
}
