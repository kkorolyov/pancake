package dev.kkorolyov.pancake.graphics.component

import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Matrix4

/**
 * Combines a set of [meshes] with the [program] to draw them.
 */
class Model(var program: Program, vararg meshes: Mesh, var offset: Matrix4? = null) : Component {
	private val _meshes = mutableListOf(*meshes)

	/**
	 * Alternate constructor for compatibility with other JVM languages.
	 */
	constructor(program: Program, meshes: List<Mesh>, offset: Matrix4? = null) : this(program, *meshes.toTypedArray(), offset = offset)

	/**
	 * Ordered model meshes.
	 */
	val meshes: List<Mesh> by ::_meshes

	/**
	 * Sets current meshes to [meshes].
	 */
	fun setMeshes(vararg meshes: Mesh) {
		_meshes.clear()
		_meshes.addAll(meshes)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Model) return false

		if (program != other.program) return false
		if (offset != other.offset) return false
		if (_meshes != other._meshes) return false
		if (meshes != other.meshes) return false

		return true
	}

	override fun hashCode(): Int {
		var result = program.hashCode()
		result = 31 * result + (offset?.hashCode() ?: 0)
		result = 31 * result + _meshes.hashCode()
		result = 31 * result + meshes.hashCode()
		return result
	}
}
