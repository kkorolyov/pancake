package dev.kkorolyov.pancake.graphics

import java.nio.ByteBuffer

/**
 * A buffer of pixel data.
 */
data class PixelBuffer(
	/**
	 * Width of the image represented by this buffer, in `px`.
	 */
	val width: Int,
	/**
	 * Height of the image represented by this buffer, in `px`.
	 */
	val height: Int,
	/**
	 * Depth of the image represented by this buffer, in `px`.
	 */
	val depth: Int,
	/**
	 * Number of components/bytes per pixel.
	 * e.g. `R` vs `RG` vs `RGB` vs `RGBA`
	 */
	val channels: Int,
	/**
	 * Underlying pixel bytes.
	 */
	val data: ByteBuffer,
	/**
	 * Frees allocated [data].
	 */
	private val free: (ByteBuffer) -> Unit
) : AutoCloseable {

	/**
	 * Flips the pixels stored in this buffer along the y-axis.
	 * For rendering APIs that expect the first pixel to be the bottom-left of the image, instead of the top left.
	 */
	fun flipVertical() {
		val rowBytes = width * channels

		for (rowI in 0 until height / 2) {
			val offset = rowI * rowBytes
			val mirrorOffset = (height - 1 - rowI) * rowBytes
			for (colI in 0 until rowBytes) {
				val i = offset + colI
				val mirrorI = mirrorOffset + colI

				val temp = data[i]
				data.put(i, data[mirrorI])
				data.put(mirrorI, temp)
			}
		}
	}

	/**
	 * Frees allocated pixel [data].
	 */
	override fun close() {
		free(data)
	}
}
