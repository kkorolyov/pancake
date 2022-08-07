package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.PixelBuffer
import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import dev.kkorolyov.pancake.graphics.resource.Texture
import org.lwjgl.opengl.GL46.*

private fun PixelBuffer.target() = if (depth > 0) GL_TEXTURE_3D else if (height > 0) GL_TEXTURE_2D else GL_TEXTURE_1D
private fun PixelBuffer.format() = if (channels == 4) GL_RGBA else if (channels == 3) GL_RGB else if (channels == 2) GL_RG else GL_R

/**
 * An `OpenGL` texture that can be reused across shared contexts.
 */
class GLTexture(
	wrapS: Wrap = Wrap.CLAMP_TO_EDGE,
	wrapT: Wrap = Wrap.CLAMP_TO_EDGE,
	filterMin: Filter = Filter.LINEAR,
	filterMag: Filter = Filter.LINEAR,
	pixels: () -> PixelBuffer
) : Texture {
	private val cache = Cache {
		pixels().use { pixels ->
			val id = glGenTextures()
			glBindTexture(pixels.target(), id)

			wrapS.let { glTexParameteri(pixels.target(), GL_TEXTURE_WRAP_S, it.value) }
			wrapT.let { glTexParameteri(pixels.target(), GL_TEXTURE_WRAP_T, it.value) }
			filterMin.let { glTexParameteri(pixels.target(), GL_TEXTURE_MIN_FILTER, it.value) }
			filterMag.let { glTexParameteri(pixels.target(), GL_TEXTURE_MAG_FILTER, it.value) }

			when (pixels.target()) {
				GL_TEXTURE_1D -> glTexImage1D(pixels.target(), 0, pixels.format(), pixels.width, 0, pixels.format(), GL_UNSIGNED_BYTE, pixels.data)
				GL_TEXTURE_2D -> glTexImage2D(pixels.target(), 0, pixels.format(), pixels.width, pixels.height, 0, pixels.format(), GL_UNSIGNED_BYTE, pixels.data)
				GL_TEXTURE_3D -> glTexImage3D(pixels.target(), 0, pixels.format(), pixels.width, pixels.height, pixels.depth, 0, pixels.format(), GL_UNSIGNED_BYTE, pixels.data)
			}
			glBindTexture(pixels.target(), 0)

			Data(id, pixels.target())
		}
	}

	/**
	 * ID of the backing `OpenGL` object.
	 */
	override val id: Int
		get() = cache().id

	/**
	 * Uses this texture as part of the current rendering state.
	 */
	override fun activate() {
		val (id, target) = cache()
		glBindTexture(target, id)
	}

	override fun deactivate() {
		val (_, target) = cache()
		glBindTexture(target, 0)
	}

	/**
	 * Deletes the backing `OpenGL` object if it has been initialized.
	 * Subsequent interactions with this texture will first initialize a new backing object.
	 */
	override fun close() {
		cache.invalidate { (id, _) -> glDeleteTextures(id) }
	}

	/**
	 * An `OpenGL` texture wrapping mode.
	 */
	enum class Wrap(internal val value: Int) {
		REPEAT(GL_REPEAT),
		MIRRORED_REPEAT(GL_MIRRORED_REPEAT),
		CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
		CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER)
	}
	/**
	 * An `OpenGL` texture filtering mode.
	 */
	enum class Filter(internal val value: Int) {
		NEAREST(GL_NEAREST),
		LINEAR(GL_LINEAR)
	}

	private data class Data(val id: Int, val target: Int)
}