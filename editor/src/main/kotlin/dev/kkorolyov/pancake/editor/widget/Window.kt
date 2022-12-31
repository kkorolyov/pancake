package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.ptr
import imgui.ImGui
import imgui.type.ImBoolean

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
	private val flags: Int = 0,
	private val setup: () -> Unit = {}
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
			setup()
			if (ImGui.begin(label, visiblePtr, flags)) {
				content()
			}
			ImGui.end()
		}
	}
}
