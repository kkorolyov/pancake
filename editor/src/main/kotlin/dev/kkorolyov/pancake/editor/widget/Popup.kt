package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import imgui.ImGui
import imgui.flag.ImGuiPopupFlags
import imgui.flag.ImGuiWindowFlags

/**
 * Draws [content] in a popup.
 */
class Popup(
	private val id: String,
	private val flags: Int = ImGuiWindowFlags.None,
) : Widget {
	/**
	 * Whether this popup is open.
	 */
	val visible: Boolean
		get() = ImGui.isPopupOpen(id)

	private var content: Widget = Widget.NOOP

	/**
	 * Sets new [content] and opens this popup.
	 */
	fun open(content: Widget, flags: Int = ImGuiPopupFlags.None) {
		this.content = content
		open(flags)
	}
	/**
	 * Opens this popup with the last-set content.
	 */
	fun open(flags: Int = ImGuiPopupFlags.None) {
		ImGui.openPopup(id, flags)
	}

	override fun invoke() {
		if (ImGui.beginPopup(id, flags)) {
			content()
			ImGui.endPopup()
		}
	}
}
