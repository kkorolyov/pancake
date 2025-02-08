package dev.kkorolyov.pancake.graphics.resource

/**
 * Combines vertex and texture data.
 */
class Mesh(
	vertices: Buffer<Vertex> = Buffer.vertex(),
	indices: Buffer<Int>? = null,
	textures: Iterable<Texture> = emptyList(),
	mode: Mode = Mode.TRIANGLES
) : RenderResource() {
	var vertices: Buffer<Vertex> = vertices
		set(value) {
			field = value
			invalidate()
		}
	var indices: Buffer<Int>? = indices
		set(value) {
			field = value
			invalidate()
		}

	private val _textures = mutableListOf<Texture>().apply { addAll(textures) }
	var textures: Iterable<Texture>
		get() = _textures
		set(value) {
			_textures.clear()
			_textures.addAll(value)
			invalidate()
		}

	var mode: Mode = mode
		set(value) {
			field = value
			invalidate()
		}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Mesh) return false

		if (vertices != other.vertices) return false
		if (indices != other.indices) return false
		if (textures != other.textures) return false
		if (mode != other.mode) return false

		return true
	}

	override fun hashCode(): Int {
		var result = vertices.hashCode()
		result = 31 * result + (indices?.hashCode() ?: 0)
		result = 31 * result + textures.hashCode()
		result = 31 * result + mode.hashCode()
		return result
	}

	/**
	 * A drawing mode.
	 */
	enum class Mode {
		TRIANGLES,
		TRIANGLE_STRIP,
		TRIANGLE_FAN
	}
}
