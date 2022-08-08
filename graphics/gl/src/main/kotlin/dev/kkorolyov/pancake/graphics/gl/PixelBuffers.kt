package dev.kkorolyov.pancake.graphics.gl

import dev.kkorolyov.pancake.graphics.PixelBuffer
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.io.InputStream

/**
 * Returns a pixel buffer from the image at [inStream].
 */
fun PixelBuffer.Companion.image(inStream: InputStream): PixelBuffer = inStream.use {
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

		PixelBuffer(width[0], height[0], 0, channels[0], stbData, STBImage::stbi_image_free)
	}
}
