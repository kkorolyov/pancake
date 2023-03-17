package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Go
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.propertiesTable
import dev.kkorolyov.pancake.editor.propertyRow
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3

class GoComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Go>(component) {
		propertiesTable("go") {
			propertyRow("Target") { input3("##${"Target"}", target) { it: Vector3 -> target.set(it) } }
			propertyRow("Strength") { input("##${"Strength"}", strength) { it: Double -> this.setStrength(it) } }
			propertyRow("Buffer") { input("##${"Buffer"}", buffer) { it: Double -> this.setBuffer(it) } }
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get<Go>(c, onNew) {
		text("TODO Go")
	}
}
