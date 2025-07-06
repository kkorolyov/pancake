package dev.kkorolyov.pancake.graphics.resource.io

import dev.kkorolyov.pancake.graphics.PixelBuffer
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext
import org.lwjgl.system.MemoryUtil

/**
 * Serializes [PixelBuffer]s.
 */
class PixelBufferSerializer : Serializer<PixelBuffer> {
	override fun write(value: PixelBuffer, context: WriteContext) {
		context.putInt(value.width)
		context.putInt(value.height)
		context.putInt(value.depth)
		context.putInt(value.channels)
		context.put(value.data)
	}

	override fun read(context: ReadContext): PixelBuffer {
		val width = context.int
		val height = context.int
		val depth = context.int
		val channels = context.int
		val data = context.get(MemoryUtil::memAlloc)

		return PixelBuffer(width, height, depth, channels, data, MemoryUtil::memFree)
	}

	override fun getType(): Class<PixelBuffer> = PixelBuffer::class.java
}
