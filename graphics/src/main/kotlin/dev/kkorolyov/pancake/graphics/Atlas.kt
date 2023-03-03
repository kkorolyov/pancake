package dev.kkorolyov.pancake.graphics

import java.nio.ByteBuffer
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * A collection of [PixelBuffer]s packed as a single buffer.
 * Provides indexed viewports to packed buffers in the order the buffers were provided.
 * Can provide custom `spacing` `px` to add between backed buffers - defaults to a value derived from the maximum buffer dimensions.
 */
class Atlas(buffers: Collection<PixelBuffer>, spacing: Int? = null) : AutoCloseable {
	/**
	 * The pixels of the packed atlas.
	 */
	val pixels: PixelBuffer

	/**
	 * Viewports to the individual buffers in the packed atlas.
	 * In the same order as the original inputs.
	 */
	val viewports: List<Viewport>

	init {
		var maxWidth = 0
		var maxChannels = 0

		// get largest buffer properties
		buffers.forEach {
			maxWidth = maxOf(maxWidth, it.width, it.height)
			maxChannels = max(maxChannels, it.channels)
		}

		// packing assumes uniform-ish square-ish textures (for simplicity)
		// calculate atlas num elements per dimension
		val elementsPerRow = ceil(sqrt(buffers.size.toDouble())).toInt()
		// add some spacing between packed buffers to minimize bleed
		val spacing = spacing ?: log2(maxWidth.toDouble()).roundToInt()
		// smallest power of 2 greater than minimum width
		val atlasWidth = 2.0.pow(ceil(log2(maxWidth.toDouble() * elementsPerRow + spacing * (elementsPerRow - 1)))).toInt()

		val data = ByteBuffer.allocateDirect(atlasWidth * atlasWidth * maxChannels)
		viewports = buffers.mapIndexed { bufferI, buffer ->
			val xI = bufferI.mod(elementsPerRow)
			val yI = bufferI / elementsPerRow
			val xPixelOffset = xI * (maxWidth + spacing)
			val yPixelOffset = yI * (maxWidth + spacing)

			val bufferOffset = xPixelOffset + yPixelOffset * atlasWidth

			// write portion into data
			(0 until buffer.data.capacity()).forEach { dataI ->
				val pixelI = dataI / buffer.channels
				val pixelOffset = bufferOffset + pixelI.mod(buffer.width) + pixelI / buffer.width * atlasWidth
				data.put(pixelOffset * maxChannels + dataI % buffer.channels, buffer.data[dataI])
			}

			// generate a viewport spanning the portion
			val viewport = Viewport(xPixelOffset.toDouble() / atlasWidth, yPixelOffset.toDouble() / atlasWidth, (xPixelOffset + buffer.width).toDouble() / atlasWidth, (yPixelOffset + buffer.height).toDouble() / atlasWidth)

			viewport
		}

		// for now, constrain to 2D textures
		pixels = PixelBuffer(atlasWidth, atlasWidth, 0, maxChannels, data) {
			// ByteBuffer gets GC-d
		}
	}

	/**
	 * Frees allocated [pixels].
	 */
	override fun close() {
		pixels.close()
	}

	/**
	 * A viewport on those pixels in an [Atlas] pertaining to an individual buffer.
	 * Coordinates are in the interval `[0.0, 1.0]` where `(0.0, 0.0)` is the origin of the overall atlas, and `(1.0, 1.0)` is the corner diagonal from the origin.
	 */
	data class Viewport(
		/**
		 * s-coordinate of the left / originating corner of the represented buffer.
		 */
		val s0: Double,
		/**
		 * t-coordinate of the left / originating corner of the represented buffer.
		 */
		val t0: Double,
		/**
		 * s-coordinate of the corner diagonal from the originating corner of the represented buffer.
		 */
		val s1: Double,
		/**
		 * t-coordinate of the corner diagonal from the originating corner of the represented buffer.
		 */
		val t1: Double
	)
}
