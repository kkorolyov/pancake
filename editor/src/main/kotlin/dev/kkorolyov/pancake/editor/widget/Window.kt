package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.ImGui
import imgui.flag.ImGuiCond
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean

private const val FULLSCREEN_FLAGS = ImGuiWindowFlags.NoDecoration or
		ImGuiWindowFlags.NoBackground or
		ImGuiWindowFlags.NoMove or
		ImGuiWindowFlags.NoBringToFrontOnFocus or
		ImGuiWindowFlags.NoFocusOnAppearing or
		ImGuiWindowFlags.NoNavFocus

/**
 * Renders [content] within a top-level window.
 */
class Window(
	/**
	 * Window title.
	 */
	val label: String,
	private val content: Widget,
	private val visiblePtr: ImBoolean = ImBoolean(true),
	private val minSize: Vector2 = Vector2.of(0.0, 0.0),
	private val maxSize: Vector2 = Vector2.of(Double.MAX_VALUE, Double.MAX_VALUE),
	flags: Int = ImGuiWindowFlags.None,
	private val fullscreen: Boolean = false
) : Widget {
	/**
	 * Whether this is visible.
	 */
	var visible: Boolean
		get() = visiblePtr.get()
		set(value) = visiblePtr.set(value)

	private val flags = if (fullscreen) flags or FULLSCREEN_FLAGS else flags

	/**
	 * Renders this as the focused window.
	 */
	fun focus() {
		ImGui.setNextWindowFocus()
		this()
	}

	override fun invoke() {
		if (visible) {
			if (fullscreen) {
				ImGui.getMainViewport().let {
					ImGui.setNextWindowPos(it.posX, it.posY)
					ImGui.setNextWindowSize(it.sizeX, it.sizeY)
				}
			} else {
				ImGui.setNextWindowSizeConstraints(minSize.x.toFloat(), minSize.y.toFloat(), maxSize.x.toFloat(), maxSize.y.toFloat())

				// on first use, set this window where the current window is
				ImGui.getWindowPos().let {
					ImGui.setNextWindowPos(it.x, it.y, ImGuiCond.FirstUseEver)
				}
			}

			if (ImGui.begin(label, visiblePtr, flags)) {
				content()
			}
			ImGui.end()
		}
	}
}
