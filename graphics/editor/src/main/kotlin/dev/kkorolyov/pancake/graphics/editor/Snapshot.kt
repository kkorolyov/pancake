package dev.kkorolyov.pancake.graphics.editor

import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Texture

/**
 * Draws meshes to a texture.
 */
interface Snapshot : AutoCloseable {
	/**
	 * Returns a texture containing the result of drawing [meshes] in order.
	 */
	operator fun invoke(meshes: List<Mesh>): Texture
}
