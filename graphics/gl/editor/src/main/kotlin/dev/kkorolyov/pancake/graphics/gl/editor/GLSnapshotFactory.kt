package dev.kkorolyov.pancake.graphics.gl.editor

import dev.kkorolyov.pancake.graphics.editor.factory.SnapshotFactory
import dev.kkorolyov.pancake.graphics.gl.resource.GLMesh
import dev.kkorolyov.pancake.graphics.resource.Mesh

class GLSnapshotFactory : SnapshotFactory {
	override fun get(c: Class<out Mesh>, width: Int, height: Int): GLSnapshot? = if (c == GLMesh::class.java) GLSnapshot(width, height) else null
}
