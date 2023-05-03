package dev.kkorolyov.pancake.graphics.gl.editor

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.graphics.resource.Mesh

class GLMeshWidgetFactory : WidgetFactory<Mesh> {
	override val type = Mesh::class.java

	override fun get(t: Mesh): Widget? {
		TODO("Not yet implemented")
	}

	override fun get(c: Class<out Mesh>, onNew: (Mesh) -> Unit): Widget? {
		TODO("Not yet implemented")
	}
}
