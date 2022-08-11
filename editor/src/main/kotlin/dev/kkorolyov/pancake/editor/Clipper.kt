package dev.kkorolyov.pancake.editor

import imgui.ImGuiListClipper
import imgui.callback.ImListClipperCallback

/**
 * Processes a clipped view of items using a given operation.
 * Useful for efficiently rendering a large list of items where only a small subset is visible at a time.
 */
class Clipper<T>(op: (T) -> Unit) {
	private val callback = ScrollCallback(op)

	/**
	 * Renders a clipped view of [items].
	 */
	operator fun invoke(items: List<T>) {
		callback.lines = items
		ImGuiListClipper.forEach(items.size, callback)
	}
}

private class ScrollCallback<T>(private val op: (T) -> Unit) : ImListClipperCallback() {
	var lines = listOf<T>()

	override fun accept(index: Int) {
		op(lines[index])
	}
}
