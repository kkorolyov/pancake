package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.graphics.gl.Pixels
import dev.kkorolyov.pancake.graphics.gl.Texture
import dev.kkorolyov.pancake.platform.Resources
import imgui.ImGui
import java.lang.IllegalArgumentException

/**
 * Renders an image from a given `texture` at the given `width` and `height`.
 */
class Image(
	private val texture: Texture,
	private val width: Float,
	private val height: Float = width
) : Widget {
	constructor(path: String, width: Float, height: Float = width) : this(
		Texture(
			wrapS = Texture.Wrap.CLAMP_TO_EDGE,
			wrapT = Texture.Wrap.CLAMP_TO_EDGE,
			filterMin = Texture.Filter.LINEAR,
			filterMag = Texture.Filter.LINEAR
		) { Pixels(Resources.inStream(path) ?: throw IllegalArgumentException("no such image: $path")) },
		width,
		height
	)

	override fun invoke() {
		ImGui.image(texture.id, width, height)
	}

	/**
	 * Renders an `ImGui` button from this image and returns its click state.
	 */
	fun clickable(): Boolean {
		return ImGui.imageButton(texture.id, width, height)
	}
}
