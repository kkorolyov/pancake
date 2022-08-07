package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.graphics.GraphicsResource

/**
 * A texture map.
 */
interface Texture : GraphicsResource {
	/**
	 * Activates this texture on the current render state.
	 */
	fun activate()
	/**
	 * Deactivates this texture on the current render state.
	 */
	fun deactivate()
}
