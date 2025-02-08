package dev.kkorolyov.pancake.graphics

import dev.kkorolyov.pancake.graphics.resource.Buffer
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.graphics.resource.Shader
import dev.kkorolyov.pancake.graphics.resource.Texture
import java.util.ServiceLoader

/**
 * All render backends on the classpath.
 */
val renderBackends: List<RenderBackend> = ServiceLoader.load(RenderBackend::class.java).toList()
/**
 * The primary render backend.
 * Defaults to the first result of [renderBackends].
 */
var renderBackend: RenderBackend = renderBackends.first()

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
	 * Sets the rendering viewport offset ([x], [y]) and size ([width] x [height]).
	 */
	fun viewport(x: Int, y: Int, width: Int, height: Int)
	/**
	 * Clears the current viewport's pixels.
	 */
	fun clear()

	/**
	 * Draws [meshes] with [program].
	 */
	fun draw(program: Program, meshes: Iterable<Mesh>)

	/**
	 * Changes the current framebuffer to [texture] and runs op.
	 * Resets to the previous framebuffer after.
	 */
	fun with(texture: Texture, op: () -> Unit)
}
