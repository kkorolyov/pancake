package dev.kkorolyov.pancake.graphics

import dev.kkorolyov.pancake.graphics.resource.Buffer
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.graphics.resource.Shader
import dev.kkorolyov.pancake.graphics.resource.Texture

/**
 * Performs low-level render operations.
 */
interface RenderBackend {
	/**
	 * Returns the ID of the internal representation of [shader].
	 */
	fun getShader(shader: Shader): Int
	/**
	 * Returns the ID of the internal representation of [program].
	 */
	fun getProgram(program: Program): Int
	/**
	 * Returns the ID of the internal representation of [texture].
	 */
	fun getTexture(texture: Texture): Int
	/**
	 * Returns the ID of the internal representation of [buffer].
	 */
	fun getBuffer(buffer: Buffer<*>): Int
	/**
	 * Returns the ID of the internal representation of [mesh].
	 */
	fun getMesh(mesh: Mesh): Int

	/**
	 * Draws [meshes] with [program].
	 */
	fun draw(program: Program, meshes: Iterable<Mesh>)
}
