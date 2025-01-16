package dev.kkorolyov.pancake.input.editor

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.input.component.InputMapper
import dev.kkorolyov.pancake.input.control.KeyControl
import dev.kkorolyov.pancake.input.control.MouseButtonControl
import dev.kkorolyov.pancake.input.event.StateEvent
import dev.kkorolyov.pancake.platform.Registry
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.flag.ImGuiPopupFlags

class InputMapperComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<InputMapper>(t) {
		Widget {
			list("##controls") {
				forEach {
					when (it) {
						is KeyControl -> keyControl(it)
						is MouseButtonControl -> mouseButtonControl(it)
					}
				}
				selectable("##empty") { }
				contextMenu(flags = ImGuiPopupFlags.MouseButtonLeft) {
					menuItem("Key") {
						this@get += KeyControl(0, StateEvent.State.PRESS) { }
					}
					menuItem("Mouse Button") {
						this@get += MouseButtonControl(0, StateEvent.State.PRESS) {}
					}
				}
			}
		}
	}

	private fun keyControl(control: KeyControl) {
		table("control", 3) {
			configColumn("Button")
			configColumn("State")
			configColumn("Action")
			headersRow()

			column {
				text(control.scanCode)
			}
			column {
				input("state", control.state) { control.state = it }
			}
			column {
				text(
					try {
						Registry.get(Action::class.java).lookup(control.action)
					} catch (e: Exception) {
						control.action
					}
				)
			}
		}
	}

	private fun mouseButtonControl(control: MouseButtonControl) {
		table("control", 3) {
			configColumn("Button")
			configColumn("State")
			configColumn("Action")
			headersRow()

			column {
				text(control.button)
			}
			column {
				input("state", control.state) { control.state = it }
			}
			column {
				text(
					try {
						Registry.get(Action::class.java).lookup(control.action)
					} catch (e: Exception) {
						control.action
					}
				)
			}
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<InputMapper>(c, onNew) {
		Widget {
			it(InputMapper())
		}
	}
}
