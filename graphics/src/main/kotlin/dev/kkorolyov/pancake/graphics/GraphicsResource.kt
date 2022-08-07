package dev.kkorolyov.pancake.graphics

/**
 * Has a lazily-allocated representation on a graphics API.
 */
interface GraphicsResource : AutoCloseable {
	/**
	 * ID of the backing representation.
	 */
	val id: Int

	/**
	 * Deletes the backing graphics API representation.
	 * Subsequent interactions with this resource will initialize a new backing representation.
	 */
	override fun close()
}
