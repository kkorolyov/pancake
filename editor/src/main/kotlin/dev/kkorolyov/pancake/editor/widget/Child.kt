package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import imgui.ImGui
import java.util.concurrent.ThreadLocalRandom

/**
 * Renders an embedded window.
 */
class Child(
	private val content: Widget,
) : Widget {
	private val id = ThreadLocalRandom.current().nextInt()

	override fun invoke() {
		if (ImGui.beginChild(id)) {
			content()
		}
		ImGui.endChild()
	}
}
