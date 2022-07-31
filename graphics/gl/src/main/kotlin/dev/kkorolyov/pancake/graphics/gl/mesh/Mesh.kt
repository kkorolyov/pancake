package dev.kkorolyov.pancake.graphics.gl.mesh

import org.lwjgl.opengl.GL46.*

/**
 * A configuration of drawable vertex data.
 * Can draw either by vertices or indices.
 * The backing `OpenGL` objects are thread-local, so accessing this mesh from different threads will affect different `OpenGL` objects.
 */
interface Mesh : AutoCloseable {
	/**
	 * ID of the referenced `OpenGL` VAO in the current thread context.
	 */
	val id: Int

	/**
	 * Draws this mesh using the current `OpenGL` context.
	 */
	fun draw()

	/**
	 * Returns a submesh using the current mode.
	 */
	fun subMesh(count: Int, offset: Int): Mesh

	/**
	 * Returns a view on this mesh drawing with [mode] [count] elements or vertices (depending on this mesh's type) starting at [offset].
	 */
	fun subMesh(count: Int, offset: Int, mode: DrawMode): Mesh

	/**
	 * Deletes the backing `OpenGL` objects in the current thread context.
	 * Subsequent access to this mesh in the current thread context will regenerate them.
	 */
	override fun close()

	/**
	 * Represents an `OpenGL` drawing mode.
	 */
	enum class DrawMode(
		/**
		 * `OpenGL` value represented by this mode.
		 */
		val value: Int
	) {
		TRIANGLES(GL_TRIANGLES),
		TRIANGLE_STRIP(GL_TRIANGLE_STRIP),
		TRIANGLE_FAN(GL_TRIANGLE_FAN)
	}

	companion object {
		/**
		 * Returns a new mesh that draws all vertices in [vertexBuffer] using [mode].
		 */
		fun vertex(vertexBuffer: VertexBuffer, mode: DrawMode): Mesh = BaseMesh(vertexBuffer, mode = mode)

		/**
		 * Returns a new mesh that draws [indices] of [vertexBuffer] using [mode].
		 */
		fun index(vertexBuffer: VertexBuffer, indices: IntArray, mode: DrawMode): Mesh = BaseMesh(vertexBuffer, indices, mode)
	}
}

private class BaseMesh(
	val vertexBuffer: VertexBuffer,
	val indices: IntArray? = null,
	val mode: Mesh.DrawMode = Mesh.DrawMode.TRIANGLES,
) : Mesh {
	private val tlData = ThreadLocal.withInitial {
		val vao = glGenVertexArrays()
		glBindVertexArray(vao)

		// bind buffers
		val vbo = glGenBuffers()
		vertexBuffer(vbo)
		val ebo = indices?.let {
			val ebo = glGenBuffers()
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, it, GL_STATIC_DRAW)
			ebo
		}

		glBindVertexArray(0)
		glBindBuffer(GL_ARRAY_BUFFER, 0)
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)

		BufferData(vao, vbo, ebo)
	}

	private val count = indices?.size ?: vertexBuffer.size

	override val id: Int
		get() = tlData.get().vao

	override fun subMesh(count: Int, offset: Int): Mesh = subMesh(count, offset, mode)
	override fun subMesh(count: Int, offset: Int, mode: Mesh.DrawMode) = SubMesh(this, count, offset, mode)

	override fun draw() {
		draw(count, 0, mode)
	}

	fun draw(count: Int, offset: Int, mode: Mesh.DrawMode) {
		glBindVertexArray(id)
		if (indices != null) glDrawElements(mode.value, count, GL_UNSIGNED_INT, offset * Int.SIZE_BYTES.toLong()) else glDrawArrays(mode.value, offset, count)
	}

	override fun close() {
		val (vao, vbo, ebo) = tlData.get()

		glDeleteVertexArrays(vao)
		glDeleteBuffers(vbo)
		ebo?.let(::glDeleteBuffers)

		tlData.remove()
	}
}

private class SubMesh(
	private val parent: BaseMesh,
	private val count: Int,
	private val offset: Int,
	private val mode: Mesh.DrawMode
) : Mesh {
	override val id: Int
		get() = parent.id

	override fun draw() {
		parent.draw(count, offset, mode)
	}

	override fun subMesh(count: Int, offset: Int) = subMesh(count, offset, mode)
	override fun subMesh(count: Int, offset: Int, mode: Mesh.DrawMode) = SubMesh(parent, count, offset, mode)

	override fun close() = parent.close()
}

private data class BufferData(val vao: Int, val vbo: Int, val ebo: Int?)
