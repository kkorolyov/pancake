package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.editor.widget.Editor
import dev.kkorolyov.pancake.editor.widget.Window
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import imgui.ImGui
import org.lwjgl.glfw.GLFW.*

/**
 * Draws editor window for a given engine.
 */
class EditorSystem(
	/**
	 * Returns engine to render info of.
	 */
	engine: () -> GameEngine,
) : GameSystem() {
	private val container by lazy { Container(glfwGetCurrentContext()) }
	private val window by lazy {
		Window("Editor", object : Widget {
			val main = Editor(engine())
			override fun invoke() {
				main()
				ImGui.showDemoWindow()
//		ImGui.showMetricsWindow()
			}
		}).apply {
			glfwSetKeyCallback(glfwGetCurrentContext()) { _, key, _, action, _ ->
				if (key == GLFW_KEY_F1 && action == GLFW_PRESS) visible = !visible
			}?.use { }
		}
	}

	override fun update(entity: Entity, dt: Long) {}

	override fun after() {
		container(window)
	}
}
