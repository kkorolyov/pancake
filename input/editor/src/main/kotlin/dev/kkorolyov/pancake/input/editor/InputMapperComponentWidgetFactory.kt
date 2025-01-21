package dev.kkorolyov.pancake.input.editor

import dev.kkorolyov.pancake.editor.Key
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.Mouse
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.combo
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.input.component.InputMapper
import dev.kkorolyov.pancake.input.control.KeyControl
import dev.kkorolyov.pancake.input.control.MouseButtonControl
import dev.kkorolyov.pancake.input.event.StateEvent
import dev.kkorolyov.pancake.platform.Registry
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.flag.ImGuiPopupFlags
import org.lwjgl.glfw.GLFW.*

class InputMapperComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<InputMapper>(t) {

		Widget {
			list("##controls", width = Layout.stretchWidth, height = Layout.free.y) {
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
			column {
				button("${getKeyName(control.key)}###key") {}
				tooltip {
					text("key - press any key to change")

					(GLFW_KEY_SPACE..GLFW_KEY_LAST).forEach {
						Key.onDown(it) {
							control.key = it
						}
					}
				}
			}
			column {
				input("##state", control.state) { control.state = it }
				tooltip("state")
			}
			column {
				val registry = Registry.get(Action::class.java)

				combo(
					"##action",
					registry.keys(),
					try {
						registry.lookup(control.action)
					} catch (e: Exception) {
						control.action.toString()
					}
				) {
					control.action = registry[it]
				}
				tooltip("action")
			}
		}
	}

	private fun mouseButtonControl(control: MouseButtonControl) {
		table("control", 3) {
			column {
				button("${getButtonName(control.button)}###button") {}
				tooltip {
					text("button - press any button to change")

					(GLFW_MOUSE_BUTTON_1..GLFW_MOUSE_BUTTON_5).forEach {
						Mouse.onDown(it) {
							control.button = it
						}
					}
				}
			}
			column {
				input("##state", control.state) { control.state = it }
				tooltip("state")
			}
			column {
				val registry = Registry.get(Action::class.java)

				combo(
					"##action",
					registry.keys(),
					try {
						registry.lookup(control.action)
					} catch (e: Exception) {
						control.action.toString()
					}
				) {
					control.action = registry[it]
				}
				tooltip("action")
			}
		}
	}

	private fun getKeyName(key: Int) = glfwGetKeyName(key, 0)
	private fun getButtonName(button: Int) = "M${button + 1}"

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<InputMapper>(c, onNew) {
		Widget {
			it(InputMapper())
		}
	}
}
