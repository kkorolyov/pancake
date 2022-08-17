package dev.kkorolyov.pancake.graphics.component

import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.platform.entity.Component

/**
 * Combines a set of meshes with the program to draw them.
 */
class Model(var program: Program, vararg meshes: Mesh) : Component {
	private val _meshes = mutableListOf(*meshes)

	/**
	 * Ordered model meshes.
	 */
	val meshes: List<Mesh>
		get() = _meshes

	/**
	 * Sets current meshes to [meshes].
	 */
	fun setMeshes(vararg meshes: Mesh) {
		_meshes.clear()
		_meshes.addAll(meshes)
	}
}
