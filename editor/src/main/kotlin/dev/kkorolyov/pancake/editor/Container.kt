package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.editor.widget.Window
import dev.kkorolyov.pancake.editor.widget.WindowManifest
import imgui.ImGui
import imgui.extension.implot.ImPlot
import imgui.flag.ImGuiConfigFlags
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import org.lwjgl.glfw.GLFW
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.io.OutputStream

private const val FLAGS =
	ImGuiConfigFlags.ViewportsEnable or
			ImGuiConfigFlags.DockingEnable or
			ImGuiConfigFlags.NavEnableKeyboard

private val log = LoggerFactory.getLogger(Container::class.java)

/**
 * Maintains an `ImGui` context and renders widget windows in the given `GLFW` `window`.
 * `flags` defaults to [ImGuiConfigFlags.ViewportsEnable] | [ImGuiConfigFlags.DockingEnable] | [ImGuiConfigFlags.NavEnableKeyboard].
 */
class Container(window: Long, flags: Int = FLAGS) : AutoCloseable {
	private val imguiGlfw: ImGuiImplGlfw
	private val imguiGl: ImGuiImplGl3

	init {
		ImGui.createContext()
		ImPlot.createContext()
		ImGui.getIO().apply {
			iniFilename = null
			addConfigFlags(flags)
		}

		imguiGlfw = ImGuiImplGlfw().apply { init(window, true) }
		imguiGl = ImGuiImplGl3().apply { init() }
	}

	/**
	 * Reads and applies GUI settings from `settings`.
	 */
	fun load(settings: InputStream) {
		// force initialize if needed
		imguiGlfw

		log.info("loading settings")
		settings.bufferedReader().use {
			ImGui.loadIniSettingsFromMemory(it.readLines().joinToString("\n"))
		}
	}
	/**
	 * Writes current GUI settings to `settings`.
	 */
	fun save(settings: OutputStream) {
		// force initialize if needed
		imguiGlfw

		log.info("saving settings")
		settings.bufferedWriter().apply {
			write(ImGui.saveIniSettingsToMemory())
			flush()
		}
		ImGui.getIO().wantSaveIniSettings = false
	}

	/**
	 * Renders [window] in this container's window.
	 */
	operator fun invoke(window: Window) {
		render(window)
	}
	/**
	 * Renders [manifest] in this container's window.
	 */
	operator fun invoke(manifest: WindowManifest<*>) {
		render(manifest)
	}

	private fun render(widget: Widget) {
		imguiGl.newFrame()
		imguiGlfw.newFrame()
		ImGui.newFrame()

		widget()

		ImGui.render()
		imguiGl.renderDrawData(ImGui.getDrawData())

		if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
			val context = GLFW.glfwGetCurrentContext()

			ImGui.updatePlatformWindows()
			ImGui.renderPlatformWindowsDefault()

			GLFW.glfwMakeContextCurrent(context)
		}
	}

	/**
	 * Disposes of this container's GUI contexts.
	 */
	override fun close() {
		imguiGl.shutdown()
		imguiGlfw.shutdown()
		ImPlot.destroyContext(ImPlot.getCurrentContext())
		ImGui.destroyContext()
	}
}
