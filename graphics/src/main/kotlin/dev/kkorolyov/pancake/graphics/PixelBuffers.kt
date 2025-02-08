package dev.kkorolyov.pancake.graphics

import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.io.InputStream

/**
 * Returns a pixel buffer from the image at [inStream].
 * Copies [inStream]'s data on construction, so callers can close the stream immediately after.
 * Can provide [flipVertical] to flip the y coordinates of pixels when loading the image.
 * [flipVertical] should be more performant than loading the image and calling [PixelBuffer.flipVertical] after.
 */
fun image(inStream: InputStream, flipVertical: Boolean = false): PixelBuffer {
	val data = inStream.readBytes().let { bytes ->
		val buffer = MemoryUtil.memAlloc(bytes.size)
		bytes.forEach(buffer::put)
		buffer.flip()
	}
	return MemoryStack.stackPush().use { stack ->
		val width = stack.mallocInt(1)
		val height = stack.mallocInt(1)
		val channels = stack.mallocInt(1)

		STBImage.stbi_set_flip_vertically_on_load(flipVertical)
		val stbData = STBImage.stbi_load_from_memory(data, width, height, channels, 0)
		MemoryUtil.memFree(data)
		if (stbData == null) throw IllegalStateException("cannot load image: $inStream")

		PixelBuffer(width[0], height[0], 0, channels[0], stbData, STBImage::stbi_image_free)
	}
}
