package dev.kkorolyov.pancake.graphics.gl.editor

import dev.kkorolyov.pancake.graphics.editor.factory.SnapshotFactory
import dev.kkorolyov.pancake.graphics.gl.resource.GLMesh
import dev.kkorolyov.pancake.graphics.resource.Mesh

class GLSnapshotFactory : SnapshotFactory {
	override fun get(mesh: Mesh, width: Int, height: Int) = (mesh as? GLMesh)?.let { GLSnapshot(width, height) }
}
