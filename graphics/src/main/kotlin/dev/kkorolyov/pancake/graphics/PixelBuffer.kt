package dev.kkorolyov.pancake.graphics

import java.nio.ByteBuffer

/**
 * A buffer of pixel data.
 */
data class PixelBuffer(
	/**
	 * Source image width.
	 */
	val width: Int,
	/**
	 * Source image height.
	 */
	val height: Int,
	/**
	 * Source image depth.
	 */
	val depth: Int,
	/**
	 * Number of components in source image.
	 * e.g. `R == 1`, `RG == 2`, `RGB == 3`, `RGBA == 4`.
	 */
	val channels: Int,
	/**
	 * Pixel data.
	 */
	val data: ByteBuffer,
	/**
	 * Frees allocated [data].
	 */
	private val free: (ByteBuffer) -> Unit
) : AutoCloseable {

	/**
	 * Frees allocated pixel [data].
	 */
	override fun close() {
		free(data)
	}

	companion object {
		/**
		 * Returns a single-element buffer of the value `255`.
		 */
		fun blank() = PixelBuffer(1, 0, 0, 1, ByteBuffer.allocateDirect(1).apply {
			put((255 and 0xff).toByte())
		}) {}
	}
}
