package dev.kkorolyov.pancake.graphics

import java.nio.ByteBuffer

/**
 * A buffer of pixel data.
 */
interface PixelBuffer : AutoCloseable {
	/**
	 * Pixel data.
	 */
	val data: ByteBuffer
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
	 * e.g. `R == 1`, `RG == 2`, `RGB == 3`, `RGBA == 4`.
	 */
	val channels: Int

	/**
	 * Frees allocated pixel [data].
	 */
	override fun close()
}
