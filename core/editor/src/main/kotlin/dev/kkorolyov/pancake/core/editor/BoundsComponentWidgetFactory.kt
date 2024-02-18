package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.propertiesTable
import dev.kkorolyov.pancake.editor.propertyRow
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.flag.ImGuiInputTextFlags

class BoundsComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Bounds>(t) {
		val width = Layout.textWidth("1000.000") * 3

		Widget {
			propertiesTable("bounds") {
				propertyRow("Vertices") {
					list("##vertices", width = width) {
						vertices.forEach { input3("##vertices", it, flags = ImGuiInputTextFlags.ReadOnly) }
					}
				}
				propertyRow("Normals") {
					list("##normals", width = width) {
						vertices.forEach { input3("##normals", it, flags = ImGuiInputTextFlags.ReadOnly) }
					}
				}
				propertyRow("Magnitude") { input("##magnitude", magnitude, flags = ImGuiInputTextFlags.ReadOnly) }
			}
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Bounds>(c, onNew) {
		var radius = 0.0
		val dimensions = Vector3.of()

		Widget {
			tree("round") {
				input("##radius", radius) { radius = it }
				tooltip("radius")

				disabledIf(radius <= 0.0) {
					button("apply") { it(Bounds.round(radius)) }
				}
			}
			tree("box") {
				input3("##dimensions", dimensions) { dimensions.set(it) }
				tooltip("dimensions")

				disabledIf(dimensions.x < 0.0 || dimensions.y < 0.0 || dimensions.z < 0.0) {
					button("apply") { it(Bounds.box(dimensions)) }
				}
			}
		}
	}
}
