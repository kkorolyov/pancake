package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ptr
import imgui.ImGui
import imgui.type.ImBoolean

/**
 * Renders a top-level window.
 */
class Window(
	/**
	 * Window title.
	 */
	var label: String,
	private val content: Widget,
	private val visiblePtr: ImBoolean = true.ptr()
) : Widget {
	/**
	 * Whether this is visible.
	 */
	var visible: Boolean
		get() = visiblePtr.get()
		set(value) = visiblePtr.set(value)

	/**
	 * Renders this as the focused window.
	 */
	fun focus() {
		ImGui.setNextWindowFocus()
		this()
	}

	override fun invoke() {
		if (visible) {
			if (ImGui.begin(label, visiblePtr)) {
				content()
			}
			ImGui.end()
		}
	}
}
