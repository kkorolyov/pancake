package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.input
import dev.kkorolyov.pancake.editor.ext.readonly
import dev.kkorolyov.pancake.editor.group
import dev.kkorolyov.pancake.editor.indented
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component

class PositionComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Position>(component) {
		group {
			text("Value")
			indented {
				value.input("##value")
			}
		}
		group {
			text("Global Value")
			indented {
				globalValue.readonly("##globalValue")
			}
		}
	}
}
