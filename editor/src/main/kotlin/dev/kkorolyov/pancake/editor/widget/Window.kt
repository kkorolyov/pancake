package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.ptr
import dev.kkorolyov.pancake.platform.math.Vector2
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
	private val minSize: Vector2 = Vector2.of(0.0, 0.0),
	private val maxSize: Vector2 = Vector2.of(Double.MAX_VALUE, Double.MAX_VALUE),
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
			ImGui.setNextWindowSizeConstraints(minSize.x.toFloat(), minSize.y.toFloat(), maxSize.x.toFloat(), maxSize.y.toFloat())
			setup()
			if (ImGui.begin(label, visiblePtr, flags) || ImGui.isWindowDocked()) {
				content()
			}
			ImGui.end()
		}
	}
}
