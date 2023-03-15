package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Go
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.ext.input
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.flag.ImGuiTableFlags

class GoComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Go>(component) {
		table("go", 2, ImGuiTableFlags.SizingStretchProp) {
			column { text("Target") }
			column { target.input("##target") }

			column { text("Strength") }
			column { input("##strength", strength, "%.3f") { strength = it } }

			column { text("Buffer") }
			column { input("##buffer", buffer, "%.3f") { buffer = it } }
		}
	}
}
