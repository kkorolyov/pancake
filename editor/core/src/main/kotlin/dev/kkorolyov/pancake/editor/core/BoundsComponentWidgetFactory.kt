package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.factory.propertiesTable
import dev.kkorolyov.pancake.editor.factory.propertyRow
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.flag.ImGuiInputTextFlags

class BoundsComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(t: Component): Widget? = ComponentWidgetFactory.get<Bounds>(t) {
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

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get<Bounds>(c, onNew) {
		text("TODO Bounds")
	}
}
