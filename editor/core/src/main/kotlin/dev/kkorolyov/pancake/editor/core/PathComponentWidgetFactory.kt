package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Path
import dev.kkorolyov.pancake.core.component.Path.SnapStrategy
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.input3
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.onFocus
import dev.kkorolyov.pancake.editor.onKey
import dev.kkorolyov.pancake.editor.propertiesTable
import dev.kkorolyov.pancake.editor.propertyRow
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.flag.ImGuiInputTextFlags
import org.lwjgl.glfw.GLFW

class PathComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Path>(t) {
		val tNewStep by ThreadLocal.withInitial(Vector3::of)

		Widget {
			propertiesTable("path") {
				propertyRow("Strength") {
					input("##strength", strength) { strength = it }
				}
				propertyRow("Proximity") {
					input("##proximity", proximity) { proximity = it }
				}
				propertyRow("Snap Strategy") {
					input("##snapStrategy", snapStrategy) { snapStrategy = it }
				}
				propertyRow("Steps") {
					list("##steps") {
						forEachIndexed { i, value -> input3("##value.${i}", value, flags = ImGuiInputTextFlags.ReadOnly) }
					}

					val newStep = tNewStep
					input3("##newStep", newStep) { newStep.set(it) }
					tooltip("<ENTER> to add step")
					onFocus {
						onKey(GLFW.GLFW_KEY_ENTER) { add(Vector3.of(newStep)) }
						onKey(GLFW.GLFW_KEY_KP_ENTER) { add(Vector3.of(newStep)) }
					}
				}
			}
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Component, Path>(c, onNew) {
		var strength = 0.0
		var proximity = 0.0
		var snapStrategy = SnapStrategy.ALL

		Widget {
			propertiesTable("path") {
				propertyRow("Strength") {
					input("##strength", strength) { strength = it }
				}
				propertyRow("Proximity") {
					input("##proximity", proximity) { proximity = it }
				}
				propertyRow("Snap Strategy") {
					input("##snapStrategy", snapStrategy) { snapStrategy = it }
				}
			}
			disabledIf(strength < 0.0 || proximity < 0.0) {
				button("apply") { it(Path(strength, proximity, snapStrategy)) }
			}
		}
	}
}
