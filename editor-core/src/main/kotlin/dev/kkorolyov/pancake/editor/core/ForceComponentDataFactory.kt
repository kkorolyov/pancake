package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.Force
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

class ForceComponentDataFactory : ComponentDataFactory {
	override fun getData(component: Component): ComponentData<*, *>? = (component as? Force)?.let(::ForceComponentData)

	override fun getDetails(data: ComponentData<*, *>): ComponentDetails? =
		(data as? ForceComponentData)?.let(::ForceComponentDetails)

	private data class ForceComponentData(private val component: Force) :
		ComponentData<Force, ForceComponentData>(component) {
		val value: ObservableStringValue
			get() = valueProperty

		private val valueProperty = stringProperty()

		override fun refresh() {
			valueProperty.set(component.value.prettyPrint())
		}

		override fun bind(other: ForceComponentData?) {
			super.bind(other)
			other?.valueProperty?.let(valueProperty::bind) ?: valueProperty.unbind()
		}
	}

	private class ForceComponentDetails(model: ForceComponentData) : ComponentDetails() {
		override val root = label(model.value)
	}
}
