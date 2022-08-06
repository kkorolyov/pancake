package dev.kkorolyov.pancake.graphics.gl

import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.io.InputStream
import java.nio.ByteBuffer

/**
 * A pixel buffer read from `inStream`.
 */
class Pixels(inStream: InputStream) : AutoCloseable {
	/**
	 * Buffer data.
	 */
	val buffer: ByteBuffer
	/**
	 * Source image width.
	 */
	val width: Int
	/**
	 * Source image height.
	 */
	val height: Int
	/**
	 * Source image depth.
	 */
	val depth: Int
	/**
	 * Number of components in source image.
	 */
	val channels: Int

	/**
	 * The `OpenGL` texture target matching this buffer.
	 */
	val target: Int
		get() = if (depth > 0) GL_TEXTURE_3D else if (height > 0) GL_TEXTURE_2D else GL_TEXTURE_1D
	/**
	 * The `OpenGL` texture format matching this buffer.
	 */
	val format: Int
		get() = if (channels == 4) GL_RGBA else if (channels == 3) GL_RGB else if (channels == 2) GL_RG else GL_R

	init {
		inStream.use {
			val data = it.readBytes().let { bytes ->
				val buffer = MemoryUtil.memAlloc(bytes.size)
				bytes.forEach(buffer::put)
				buffer.flip()
			}
			MemoryStack.stackPush().use { stack ->
				val width = stack.mallocInt(1)
				val height = stack.mallocInt(1)
				val channels = stack.mallocInt(1)

				val stbData = STBImage.stbi_load_from_memory(data, width, height, channels, 0)
				MemoryUtil.memFree(data)

				if (stbData == null) throw IllegalStateException("cannot load image: $inStream")

				buffer = stbData
				this.width = width[0]
				this.height = height[0]
				depth = 0
				this.channels = channels[0]
			}
		}
	}

	/**
	 * Frees the backing buffer.
	 */
	override fun close() {
		STBImage.stbi_image_free(buffer)
	}
}
