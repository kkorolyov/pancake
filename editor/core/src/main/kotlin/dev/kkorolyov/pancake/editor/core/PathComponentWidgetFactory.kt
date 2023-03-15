package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Path
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.group
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.sameLine
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.flag.ImGuiInputTextFlags

private val tNewStep = ThreadLocal.withInitial(Vector3::of)

class PathComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Path>(component) {
		list("##steps") {
			forEachIndexed { i, value -> input3("##value.${i}", value, flags = ImGuiInputTextFlags.ReadOnly) }
		}

		val newStep = tNewStep.get()
		group {
			button("+") { add(newStep) }
			sameLine()
			input3("##newStep", newStep) { newStep.set(it) }
		}
		tooltip("add step")
	}
}
