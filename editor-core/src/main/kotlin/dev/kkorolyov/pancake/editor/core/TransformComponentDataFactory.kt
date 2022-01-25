package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.editor.ComponentDataFactory
import dev.kkorolyov.pancake.editor.ComponentData
import dev.kkorolyov.pancake.editor.prettyPrint
import dev.kkorolyov.pancake.editor.ComponentDetails
import dev.kkorolyov.pancake.platform.entity.Component
import javafx.beans.value.ObservableValue
import tornadofx.field
import tornadofx.fieldset
import tornadofx.form
import tornadofx.label
import tornadofx.objectProperty

class TransformComponentDataFactory : ComponentDataFactory {
	override fun getData(component: Component): ComponentData<*, *>? =
		(component as? Transform)?.let(::TransformComponentData)

	override fun getDetails(data: ComponentData<*, *>): ComponentDetails? =
		(data as? TransformComponentData)?.let(::TransformComponentDetails)

	private data class TransformComponentData(private val component: Transform? = null) :
		ComponentData<Transform, TransformComponentData>(component) {
		val position: ObservableValue<String>
			get() = positionProperty
		private val positionProperty = objectProperty<String>()

		override fun refresh() {
			component?.let {
				positionProperty.set(prettyPrint(it.position))
			}
		}

		override fun bind(other: TransformComponentData?) {
			super.bind(other)
			other?.positionProperty?.let(positionProperty::bind) ?: positionProperty.unbind()
		}

	}

	private class TransformComponentDetails(private val model: TransformComponentData) : ComponentDetails() {
		override val root = form {
			fieldset {
				field("Position") {
					label(model.position)
				}
			}
		}
	}
}
