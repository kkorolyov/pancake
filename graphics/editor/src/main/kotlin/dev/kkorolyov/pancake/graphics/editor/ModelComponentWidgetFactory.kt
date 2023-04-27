package dev.kkorolyov.pancake.graphics.editor

import dev.kkorolyov.pancake.editor.MemoizedContent
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.image
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.sameLine
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.graphics.component.Model
import dev.kkorolyov.pancake.graphics.editor.factory.getSnapshot
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.platform.entity.Component

class ModelComponentWidgetFactory : WidgetFactory<Component> {
	override val type = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Model>(t) {
		val width = 128
		val height = 128
		val mesh = MemoizedContent<Mesh>({ getWidget(Mesh::class.java, it) })
		val allMeshes = MemoizedContent<List<Mesh>>({
			val snapshot = getSnapshot(it.first(), width, height)
			Widget { image(snapshot(it), width.toFloat(), height.toFloat()) }
		})

		Widget {
			if (meshes.isNotEmpty()) {
				allMeshes(meshes)
				allMeshes.value()
			}

			text("program: ${program.id}")
			sameLine()
			button("free") { program.close() }

			text("meshes")
			list("##meshes") {
				meshes.forEach {
					selectable(it.id.toString()) { it.close() }
					tooltip {
						mesh(it)
						mesh.value()
					}
				}
			}
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? {
		TODO("Not yet implemented")
	}
}
