package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.movement.Mass
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.ptr
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.ImGui

class MassComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Mass>(component) {
		val ptr = value.ptr()
		ImGui.inputDouble("##value", ptr, 0.0001, 1.0, "%.4f")
		value = ptr.get()
	}
}
