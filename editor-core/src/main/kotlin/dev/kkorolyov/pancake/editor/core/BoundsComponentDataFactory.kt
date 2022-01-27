package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.editor.ComponentData
import dev.kkorolyov.pancake.editor.ComponentDataFactory
import dev.kkorolyov.pancake.editor.ComponentDetails
import dev.kkorolyov.pancake.editor.prettyPrint
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import javafx.beans.value.ObservableDoubleValue
import javafx.collections.ObservableList
import tornadofx.doubleProperty
import tornadofx.field
import tornadofx.fieldset
import tornadofx.form
import tornadofx.label
import tornadofx.listview
import tornadofx.observableListOf

class BoundsComponentDataFactory : ComponentDataFactory {
	override fun getData(component: Component): ComponentData<*, *>? =
		(component as? Bounds)?.let(::BoundsComponentData)

	override fun getDetails(data: ComponentData<*, *>): ComponentDetails? =
		(data as? BoundsComponentData)?.let(::BoundsComponentDetails)

	private data class BoundsComponentData(private val component: Bounds) :
		ComponentData<Bounds, BoundsComponentData>(component) {
		val vertices: ObservableList<String> = observableListOf()
		val normals: ObservableList<String> = observableListOf()
		val magnitude: ObservableDoubleValue
			get() = magnitudeProperty

		private val magnitudeProperty = doubleProperty()

		override fun refresh() {
			vertices.setAll(component.vertices.map(Vector3::prettyPrint))
			normals.setAll(component.normals.map(Vector2::prettyPrint))
			magnitudeProperty.set(component.magnitude)
		}

		override fun bind(other: BoundsComponentData?) {
			super.bind(other)
			other?.vertices?.let(vertices::setAll) ?: vertices.clear()
			other?.normals?.let(normals::setAll) ?: normals.clear()
		}
	}

	private class BoundsComponentDetails(model: BoundsComponentData) : ComponentDetails() {
		override val root = form {
			fieldset {
				label("Vertices")
				listview(model.vertices) {
					maxHeight = 96.0
				}

				label("Normals")
				listview(model.normals) {
					maxHeight = 96.0
				}

				field("Magnitude") {
					label(model.magnitude)
				}
			}
		}
	}
}
