package dev.kkorolyov.pancake.graphics.editor

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.input2
import dev.kkorolyov.pancake.editor.propertiesTable
import dev.kkorolyov.pancake.editor.propertyRow
import dev.kkorolyov.pancake.graphics.component.Lens
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.flag.ImGuiInputTextFlags

class LensComponentWidgetFactory : WidgetFactory<Component> {
	override val type = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Lens>(t) {
		Widget {
			propertiesTable("lens") {
				propertyRow("Scale") {
					input2("##scale", scale, onChange = scale::set)
				}
				propertyRow("Size") {
					input2("##size", size, onChange = size::set)
				}
				propertyRow("Offset") {
					input2("##offset", offset, onChange = offset::set)
				}
				propertyRow("Mask") {
					input("##mask", mask.toString(), flags = ImGuiInputTextFlags.ReadOnly) {}
				}
				propertyRow("Active") {
					input("##active", active) { active = it }
				}
			}
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Lens>(c, onNew) {
		val scale = Vector2.of()
		val size = Vector2.of()
		val offset = Vector2.of()
		var active = false

		Widget {
			propertiesTable("lens") {
				propertyRow("Scale") {
					input2("##scale", scale, onChange = scale::set)
				}
				propertyRow("Size") {
					input2("##size", size, onChange = size::set)
				}
				propertyRow("Offset") {
					input2("##offset", offset, onChange = offset::set)
				}
				propertyRow("Active") {
					input("##active", active) { active = it }
				}
			}
			disabledIf(scale.x <= 0.0 || scale.y <= 0.0 || size.x <= 0.0 || size.y <= 0.0) {
				button("apply") {
					it(Lens(scale, size, offset, active = active))
				}
			}
		}
	}
}
