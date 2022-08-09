package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.PixelBuffer
import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import dev.kkorolyov.pancake.graphics.resource.Texture
import org.lwjgl.opengl.GL46.*

private fun PixelBuffer.target() = if (depth > 0) GL_TEXTURE_3D else if (height > 0) GL_TEXTURE_2D else GL_TEXTURE_1D
private fun PixelBuffer.format() = if (channels == 4) GL_RGBA8 to GL_RGBA else if (channels == 3) GL_RGB8 to GL_RGB else if (channels == 2) GL_RG8 to GL_RG else GL_R8 to GL_R

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
			val id = glCreateTextures(pixels.target())

			wrapS.let { glTextureParameteri(id, GL_TEXTURE_WRAP_S, it.value) }
			wrapT.let { glTextureParameteri(id, GL_TEXTURE_WRAP_T, it.value) }
			filterMin.let { glTextureParameteri(id, GL_TEXTURE_MIN_FILTER, it.value) }
			filterMag.let { glTextureParameteri(id, GL_TEXTURE_MAG_FILTER, it.value) }

			val (internalFormat, format) = pixels.format()
			when (pixels.target()) {
				GL_TEXTURE_1D -> {
					glTextureStorage1D(id, 1, internalFormat, pixels.width)
					glTextureSubImage1D(id, 0, 0, pixels.width, format, GL_UNSIGNED_BYTE, pixels.data)
				}

				GL_TEXTURE_2D -> {
					glTextureStorage2D(id, 1, internalFormat, pixels.width, pixels.height)
					glTextureSubImage2D(id, 0, 0, 0, pixels.width, pixels.height, format, GL_UNSIGNED_BYTE, pixels.data)
				}

				GL_TEXTURE_3D -> {
					glTextureStorage3D(id, 1, internalFormat, pixels.width, pixels.height, pixels.depth)
					glTextureSubImage3D(id, 0, 0, 0, 0, pixels.width, pixels.height, pixels.depth, format, GL_UNSIGNED_BYTE, pixels.data)
				}
			}

			id
		}
	}

	/**
	 * ID of the backing `OpenGL` object.
	 */
	override val id by cache

	/**
	 * Deletes the backing `OpenGL` object if it has been initialized.
	 * Subsequent interactions with this texture will first initialize a new backing object.
	 */
	override fun close() {
		cache.invalidate(::glDeleteTextures)
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
}
