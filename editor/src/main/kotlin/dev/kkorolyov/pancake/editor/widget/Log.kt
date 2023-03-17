package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Clipper
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.text
import imgui.ImGui
import java.io.ByteArrayOutputStream

/**
 * Renders lines from [data] as a scrolling log.
 */
class Log(private val data: ByteArrayOutputStream) : Widget {
	private val clipper = Clipper<String>(::text)

	override fun invoke() {
		button("Clear") { data.reset() }
		list("##log") {
			clipper(data.toString().lines())

			if (ImGui.getScrollY() >= ImGui.getScrollMaxY()) ImGui.setScrollHereY()
		}
	}
}
