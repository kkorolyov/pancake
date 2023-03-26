package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean

/**
 * Draws [content] within a modal.
 */
class Modal(
	/**
	 * Modal title.
	 */
	val label: String,
	private val content: Widget,
	private val open: ImBoolean = ImBoolean(true),
	private val minSize: Vector2 = Vector2.of(0.0, 0.0),
	private val maxSize: Vector2 = Vector2.of(Double.MAX_VALUE, Double.MAX_VALUE),
	private val flags: Int = ImGuiWindowFlags.None
) : Widget {
	private val visiblePtr = ImBoolean(open.get())
	/**
	 * Whether this is open.
	 */
	var visible: Boolean
		get() = visiblePtr.get()
		set(value) {
			if (value && !visiblePtr.get()) {
				open.set(true)
			}
			visiblePtr.set(value)
		}

	override fun invoke() {
		if (open.get()) {
			ImGui.openPopup(label)
			open.set(false)
		}
		if (visible) {
			ImGui.setNextWindowSizeConstraints(minSize.x.toFloat(), minSize.y.toFloat(), maxSize.x.toFloat(), maxSize.y.toFloat())
		}
		if (ImGui.beginPopupModal(label, visiblePtr, flags)) {
			content()
			ImGui.endPopup()
		}
	}
}
