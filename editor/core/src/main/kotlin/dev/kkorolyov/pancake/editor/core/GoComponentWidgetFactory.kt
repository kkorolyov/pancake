package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Go
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.factory.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.factory.propertiesTable
import dev.kkorolyov.pancake.editor.factory.propertyRow
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3

class GoComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(t: Component): Widget? = ComponentWidgetFactory.get<Go>(t) {
		Widget {
			propertiesTable("go") {
				propertyRow("Target") { input3("##${"Target"}", target) { it: Vector3 -> target.set(it) } }
				propertyRow("Strength") { input("##${"Strength"}", strength) { it: Double -> this.setStrength(it) } }
				propertyRow("Buffer") { input("##${"Buffer"}", buffer) { it: Double -> this.setBuffer(it) } }
			}
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = ComponentWidgetFactory.get(c, onNew) {
		val target = Vector3.of()
		var strength = 0.0
		var buffer = 0.0

		Widget {
			propertiesTable("go") {
				propertyRow("Target") { input3("##${"Target"}", target) { target.set(it) } }
				propertyRow("Strength") { input("##${"Strength"}", strength) { strength = it } }
				propertyRow("Buffer") { input("##${"Buffer"}", buffer) { buffer = it } }
			}
			disabledIf(strength < 0.0 || buffer < 0.0) {
				button("apply") { it(Go(target, strength, buffer)) }
			}
		}
	}
}
