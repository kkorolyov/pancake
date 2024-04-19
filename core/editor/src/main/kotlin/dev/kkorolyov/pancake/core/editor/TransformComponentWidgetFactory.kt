package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.editor.Key
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.Mouse
import dev.kkorolyov.pancake.editor.Style
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.dragInput
import dev.kkorolyov.pancake.editor.dragInput3
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.group
import dev.kkorolyov.pancake.editor.sameLine
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.glfw.GLFW

class TransformComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Transform>(t) {
		val rotInterval = 1.0 / 180.0 * Math.PI
		val xAxis = Vector3.of(1.0)
		val yAxis = Vector3.of(0.0, 1.0)
		val zAxis = Vector3.of(0.0, 0.0, 1.0)
		var rotX = 0
		var rotY = 0
		var rotZ = 0
		var rotating = false

		Widget {
			Layout.width(Layout.stretchWidth) {
				dragInput3("##translation", translation) { translation.set(it) }
			}
			tooltip("translation")

			group {
				val width = (Layout.stretchWidth - Style.spacing.x) / 3

				Layout.width(width) {
					dragInput("##rotX", rotX) {
						rotating = true
						val delta = it - rotX
						rotX = it
						if (delta != 0) rotation.rotate(rotInterval * delta, xAxis)
					}
				}
				sameLine()
				Layout.cursor.x -= 0.5f * Style.spacing.x
				Layout.width(width) {
					dragInput("##rotY", rotY) {
						rotating = true
						val delta = it - rotY
						rotY = it
						if (delta != 0) rotation.rotate(rotInterval * -delta, yAxis)
					}
				}
				sameLine()
				Layout.cursor.x -= 0.5f * Style.spacing.x
				Layout.width(width) {
					dragInput("##rotZ", rotZ) {
						rotating = true
						val delta = it - rotZ
						rotZ = it
						if (delta != 0) rotation.rotate(rotInterval * -delta, zAxis)
					}
				}

				if (rotating && (Mouse.onRelease() || Key.onRelease(GLFW.GLFW_KEY_ENTER) || Key.onRelease(GLFW.GLFW_KEY_KP_ENTER) || Key.onRelease(GLFW.GLFW_KEY_TAB))) {
					rotX = 0
					rotY = 0
					rotZ = 0
				}
			}
			tooltip("rotation")

			Layout.width((Layout.stretchWidth)) {
				dragInput3("##scale", scale) { scale.set(it) }
			}
			tooltip("scale")
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Transform>(c, onNew) {
		val translation = Vector3.of()
		val scale = Vector3.of(1.0, 1.0, 1.0)

		Widget {
			dragInput3("##translation", translation) { translation.set(it) }
			tooltip("translation")

			dragInput3("##scale", scale) { scale.set(it) }
			tooltip("scale")

			button("apply") {
				it(Transform().apply {
					this.translation.set(translation)
					this.scale.set(scale)
				})
			}
		}
	}
}
