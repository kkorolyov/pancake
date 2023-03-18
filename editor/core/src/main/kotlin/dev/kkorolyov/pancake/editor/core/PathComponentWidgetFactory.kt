package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Path
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.onFocus
import dev.kkorolyov.pancake.editor.onKey
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.flag.ImGuiInputTextFlags
import org.lwjgl.glfw.GLFW

private val tNewStep by ThreadLocal.withInitial(Vector3::of)

class PathComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(t: Component): Widget? = ComponentWidgetFactory.get<Path>(t) {
		list("##steps") {
			forEachIndexed { i, value -> input3("##value.${i}", value, flags = ImGuiInputTextFlags.ReadOnly) }
		}

		val newStep = tNewStep
		input3("##newStep", newStep) { newStep.set(it) }
		tooltip("<ENTER> to add step")
		onFocus {
			onKey(GLFW.GLFW_KEY_ENTER) { add(newStep) }
			onKey(GLFW.GLFW_KEY_KP_ENTER) { add(newStep) }
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get<Path>(c, onNew) {
		text("TODO Path")
	}
}
