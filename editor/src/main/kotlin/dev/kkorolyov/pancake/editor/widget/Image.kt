package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.platform.Resources
import imgui.ImGui
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage.STBI_rgb_alpha
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import org.lwjgl.system.MemoryStack

/**
 * Renders an image at a given path.
 */
class Image private constructor(
	private val tlData: ThreadLocal<ImageData>,
	private val width: Float? = null,
	private val height: Float? = null
) : Widget, AutoCloseable {
	/**
	 * Returns an image that renders at its native size.
	 */
	constructor(
		/**
		 * Image path.
		 */
		path: String
	) : this(ThreadLocal.withInitial {
		Resources.inStream(path)?.use {
			val data = it.readBytes().let { bytes ->
				val buffer = BufferUtils.createByteBuffer(bytes.size)
				bytes.forEach(buffer::put)
				buffer.flip()
				buffer
			}
			MemoryStack.stackPush().use { stack ->
				val width = stack.mallocInt(1)
				val height = stack.mallocInt(1)

				stbi_load_from_memory(data, width, height, stack.mallocInt(1), STBI_rgb_alpha)?.let { stbData ->
					val id = glGenTextures()
					glBindTexture(GL_TEXTURE_2D, id)

					glEnable(GL_BLEND)
					glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)

					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

					glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, stbData)
					glBindTexture(GL_TEXTURE_2D, 0)
					stbi_image_free(stbData)

					ImageData(id, width[0].toFloat(), height[0].toFloat())
				} ?: throw IllegalArgumentException("no such image: $path")
			}
		}
	})

	/**
	 * Returns a variant of this image rendering at custom [width] and [height].
	 */
	fun withSize(width: Float, height: Float): Image = Image(tlData, width, height)

	override fun invoke() {
		val (id, width, height) = tlData.get()
		ImGui.image(id, this.width ?: width, this.height ?: height)
	}

	/**
	 * Renders an `ImGui` button from this image and returns its click state.
	 */
	fun clickable(): Boolean {
		val (id, width, height) = tlData.get()
		return ImGui.imageButton(id, this.width ?: width, this.height ?: height)
	}

	/**
	 * Deletes the backing `OpenGL` objects in the current thread context.
	 * Subsequent access to this image in the current thread context will regenerate them.
	 */
	override fun close() {
		glDeleteTextures(tlData.get().id)
		tlData.remove()
	}
}

private data class ImageData(val id: Int, val width: Float, val height: Float)
