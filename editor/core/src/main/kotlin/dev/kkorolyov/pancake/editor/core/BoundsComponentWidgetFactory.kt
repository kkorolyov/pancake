package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.factory.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.factory.propertiesTable
import dev.kkorolyov.pancake.editor.factory.propertyRow
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.flag.ImGuiInputTextFlags

class BoundsComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(t: Component): Widget? = ComponentWidgetFactory.get<Bounds>(t) {
		Widget {
			propertiesTable("bounds") {
				propertyRow("Vertices") {
					list("##vertices") {
						vertices.forEach { input3("##vertices", it, flags = ImGuiInputTextFlags.ReadOnly) }
					}
				}
				propertyRow("Normals") {
					list("##normals") {
						vertices.forEach { input3("##normals", it, flags = ImGuiInputTextFlags.ReadOnly) }
					}
				}
				propertyRow("Magnitude") { input("##magnitude", magnitude, flags = ImGuiInputTextFlags.ReadOnly) }
			}
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get<Bounds>(c, onNew) {
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
