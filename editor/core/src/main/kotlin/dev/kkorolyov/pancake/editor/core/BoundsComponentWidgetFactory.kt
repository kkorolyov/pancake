package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.readonly
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.ImGui

class BoundsComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Bounds>(component) {
		tree("Vertices") {
			list("##vertices") {
				vertices.forEach { it.readonly() }
			}
		}
		tree("Normals") {
			list("##normals") {
				normals.forEach { it.readonly() }
			}
		}
		tree("Magnitude") {
			ImGui.text(magnitude.toString())
		}
	}
}
