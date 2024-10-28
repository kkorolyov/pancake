package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.graphics.resource.Texture
import imgui.ImGui

/**
 * Renders an image from a given `texture` at the given `width` and `height`.
 */
class Image(
	private val texture: Texture,
	private val width: Float,
	private val height: Float = width
) : Widget {
	override fun invoke() {
		ImGui.image(texture.id.toLong(), width, height)
	}

	/**
	 * Renders an `ImGui` button from this image and returns its click state.
	 */
	fun clickable(): Boolean {
		return ImGui.imageButton(texture.id.toLong(), width, height)
	}
}
