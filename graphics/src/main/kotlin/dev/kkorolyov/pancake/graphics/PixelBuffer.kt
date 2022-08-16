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
		 * Returns a 1D single-element buffer of the value `255`.
		 */
		fun blank1() = blank(1)
		/**
		 * Returns a 2D single-element buffer of the value `255`.
		 */
		fun blank2() = blank(1, 1)
		/**
		 * Returns a 3D single-element buffer of the value `255`.
		 */
		fun blank3() = blank(1, 1, 1)

		private fun blank(width: Int = 0, height: Int = 0, depth: Int = 0) = PixelBuffer(width, height, depth, 4, ByteBuffer.allocateDirect(4).apply {
			val value = (255 and 0xff).toByte()

			(0 until capacity()).forEach {
				put(it, value)
			}
		}) {
			// no need to free manually - ByteBuffer has a Cleaner that will free on GC
		}
	}
}
