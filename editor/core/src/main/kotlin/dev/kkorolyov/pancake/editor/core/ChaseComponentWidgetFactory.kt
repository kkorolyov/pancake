package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Chase
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.input
import dev.kkorolyov.pancake.editor.ext.ptr
import dev.kkorolyov.pancake.editor.group
import dev.kkorolyov.pancake.editor.indented
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.ImGui

class ChaseComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Chase>(component) {
		group {
			text("Target")
			indented {
				target.input("##target")
			}
		}
		group {
			text("Strength")
			indented {
				val ptr = strength.ptr()
				ImGui.inputDouble("Strength", ptr)
				strength = ptr.get()
			}
		}
		group {
			text("Buffer")
			indented {
				val ptr = buffer.ptr()
				ImGui.inputDouble("Buffer", ptr)
				buffer = ptr.get()
			}
		}
	}
}
