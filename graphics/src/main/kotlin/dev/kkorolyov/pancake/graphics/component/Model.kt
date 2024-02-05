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
}
