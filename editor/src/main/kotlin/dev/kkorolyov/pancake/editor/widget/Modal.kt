package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.ImGui
import imgui.flag.ImGuiPopupFlags
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
	private val minSize: Vector2 = Vector2.of(0.0, 0.0),
	private val maxSize: Vector2 = Vector2.of(Double.MAX_VALUE, Double.MAX_VALUE),
	private val flags: Int = ImGuiWindowFlags.None
) : Widget {
	private val visiblePtr = ImBoolean()
	/**
	 * Whether this is open.
	 */
	val visible: Boolean
		get() = ImGui.isPopupOpen(label)

	private var content: Widget = Widget.NOOP

	/**
	 * Sets new [content] and opens this modal.
	 */
	fun open(content: Widget, flags: Int = ImGuiPopupFlags.None) {
		this.content = content
		open(flags)
	}
	/**
	 * Opens this modal with the last-set content.
	 */
	fun open(flags: Int = ImGuiPopupFlags.None) {
		ImGui.openPopup(label, flags)
		visiblePtr.set(true)
	}

	/**
	 * Closes this modal.
	 */
	fun close() {
		visiblePtr.set(false)
	}

	override fun invoke() {
		if (visible) {
			ImGui.setNextWindowSizeConstraints(minSize.x.toFloat(), minSize.y.toFloat(), maxSize.x.toFloat(), maxSize.y.toFloat())

			if (ImGui.beginPopupModal(label, visiblePtr, flags)) {
				content()
				ImGui.endPopup()
			}
		}
	}
}
