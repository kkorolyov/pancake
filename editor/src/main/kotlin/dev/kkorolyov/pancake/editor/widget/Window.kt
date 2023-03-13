package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.ptr
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.ImGui
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
 * Can optionally be provided [flags] and additional [setup] to run before rendering.
 */
class Window(
	/**
	 * Window title.
	 */
	var label: String,
	private val content: Widget,
	private val visiblePtr: ImBoolean = true.ptr(),
	private val minSize: Vector2 = Vector2.of(0.0, 0.0),
	private val maxSize: Vector2 = Vector2.of(Double.MAX_VALUE, Double.MAX_VALUE),
	private var flags: Int = 0,
	private val fullscreen: Boolean = false
) : Widget {
	/**
	 * Whether this is visible.
	 */
	var visible: Boolean
		get() = visiblePtr.get()
		set(value) = visiblePtr.set(value)

	init {
		if (fullscreen) flags = flags or FULLSCREEN_FLAGS
	}

	/**
	 * Renders this as the focused window.
	 */
	fun focus() {
		ImGui.setNextWindowFocus()
		this()
	}

	override fun invoke() {
		if (visible) {
			ImGui.setNextWindowSizeConstraints(minSize.x.toFloat(), minSize.y.toFloat(), maxSize.x.toFloat(), maxSize.y.toFloat())
			if (fullscreen) {
				ImGui.getMainViewport().let { ImGui.setNextWindowPos(it.posX, it.posY) }
				ImGui.getIO().displaySize.let { ImGui.setNextWindowSize(it.x, it.y) }
			}
			// ensure shown in cases of child/parent windows docked to it - to avoid visibility feedback loop
			// FIXME this doesn't handle cases of grand-child/parent windows docked to this window
			if (ImGui.begin(label, visiblePtr, flags) || ImGui.isWindowDocked()) {
				content()
			}
			ImGui.end()
		}
	}
}
