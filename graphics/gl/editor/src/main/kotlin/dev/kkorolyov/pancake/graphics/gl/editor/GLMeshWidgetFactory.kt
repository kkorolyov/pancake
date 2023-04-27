package dev.kkorolyov.pancake.graphics.gl.editor

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.image
import dev.kkorolyov.pancake.graphics.gl.resource.GLMesh
import dev.kkorolyov.pancake.graphics.resource.Mesh

private const val width = 128
private const val height = 128

class GLMeshWidgetFactory : WidgetFactory<Mesh> {
	private val snapshot = GLSnapshot(width, height)

	override val type = Mesh::class.java

	override fun get(t: Mesh): Widget? = WidgetFactory.get<GLMesh>(t) {
		val meshes = listOf(t)

		Widget {
			image(snapshot(meshes), width.toFloat(), height.toFloat())
		}
	}

	override fun get(c: Class<Mesh>, onNew: (Mesh) -> Unit): Widget? {
		TODO("Not yet implemented")
	}
}
