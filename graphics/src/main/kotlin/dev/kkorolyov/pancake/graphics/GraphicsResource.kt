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

	/**
	 * A resource that must be explicitly activated to apply to the current render state.
	 */
	interface Active : GraphicsResource {
		/**
		 * Activates this resource on the current render state.
		 */
		fun activate()
		/**
		 * Deactivates this resource on the current render state.
		 */
		fun deactivate()
	}
}

/**
 * Activates this resource, runs [op], then deactivates this resource.
 */
inline fun GraphicsResource.Active.scoped(op: () -> Unit) {
	activate()
	op()
	deactivate()
}
