package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.editor.widget.Window
import imgui.ImGui
import imgui.flag.ImGuiConfigFlags
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import org.lwjgl.glfw.GLFW

private const val FLAGS =
	ImGuiConfigFlags.ViewportsEnable or
			ImGuiConfigFlags.DockingEnable or
			ImGuiConfigFlags.NavEnableKeyboard

/**
 * Renders widget windows in the given `GLFW` [window].
 */
class Container(private val window: Long, private val flags: Int = FLAGS) {
	private val imguiGlfw by lazy {
		ImGui.createContext()
		ImGui.getIO().apply {
			iniFilename = null
			addConfigFlags(flags)
		}

		ImGuiImplGlfw().apply {
			init(window, true)
			imguiGl = ImGuiImplGl3().apply { init() }
		}
	}
	private lateinit var imguiGl: ImGuiImplGl3

	/**
	 * Renders widget [Window] in this container's window.
	 */
	operator fun invoke(window: Window) {
		imguiGlfw.newFrame()
		ImGui.newFrame()

		window()

		ImGui.render()
		imguiGl.renderDrawData(ImGui.getDrawData())

		val context = GLFW.glfwGetCurrentContext()
		ImGui.updatePlatformWindows()
		ImGui.renderPlatformWindowsDefault()
		GLFW.glfwMakeContextCurrent(context)
	}
}
