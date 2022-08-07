package dev.kkorolyov.pancake.graphics.gl

import dev.kkorolyov.pancake.graphics.PixelBuffer
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.io.InputStream
import java.nio.ByteBuffer

/**
 * Buffers pixel data from the image at `inStream`.
 */
class ImageData(inStream: InputStream) : PixelBuffer {
	override val data: ByteBuffer
	override val width: Int
	override val height: Int
	override val depth: Int
	override val channels: Int

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

				this.data = stbData
				this.width = width[0]
				this.height = height[0]
				depth = 0
				this.channels = channels[0]
			}
		}
	}

	override fun close() {
		STBImage.stbi_image_free(data)
	}
}
