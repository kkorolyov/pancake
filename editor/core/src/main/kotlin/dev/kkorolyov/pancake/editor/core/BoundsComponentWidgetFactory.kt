package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.propertiesTable
import dev.kkorolyov.pancake.editor.propertyRow
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.flag.ImGuiInputTextFlags

class BoundsComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Bounds>(component) {
		propertiesTable("bounds") {
			propertyRow("Vertices") {
				list("Vertices") {
					vertices.forEach { input3("##vertices", it, flags = ImGuiInputTextFlags.ReadOnly) }
				}
			}
			propertyRow("Normals") {
				list("Normals") {
					vertices.forEach { input3("##normals", it, flags = ImGuiInputTextFlags.ReadOnly) }
				}
			}
			propertyRow("Magnitude") { input("##magnitude", magnitude, flags = ImGuiInputTextFlags.ReadOnly) }
		}
	}
}
