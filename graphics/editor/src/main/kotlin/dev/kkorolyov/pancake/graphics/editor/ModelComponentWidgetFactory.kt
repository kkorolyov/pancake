package dev.kkorolyov.pancake.graphics.editor

import dev.kkorolyov.pancake.editor.MemoizedContent
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.image
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.sameLine
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.editor.widget.Modal
import dev.kkorolyov.pancake.graphics.component.Model
import dev.kkorolyov.pancake.graphics.editor.factory.getSnapshot
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.platform.entity.Component

private const val IMAGE_WIDTH = 128
private const val IMAGE_HEIGHT = 128

private val snapshots = mutableListOf<MutableMap<Class<out Mesh>, Snapshot>>()

private fun getSharedSnapshot(i: Int, c: Class<out Mesh>) = snapshots.getOrElse(i) {
	for (j in (snapshots.size..i)) {
		snapshots.add(mutableMapOf())
	}
	snapshots[i]
}.getOrPut(c) { getSnapshot(c) }

class ModelComponentWidgetFactory : WidgetFactory<Component> {
	override val type = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Model>(t) {
		val allMeshes = MemoizedContent<List<Mesh>>({
			val texture = getSharedSnapshot(0, it.first()::class.java)(it)
			Widget { image(texture, IMAGE_WIDTH.toFloat(), IMAGE_HEIGHT.toFloat()) }
		})
		val mesh = MemoizedContent<Mesh>({
			val texture = getSharedSnapshot(1, it::class.java)(listOf(it))
			Widget { image(texture, IMAGE_WIDTH.toFloat(), IMAGE_HEIGHT.toFloat()) }
		})

		val editProgram = Modal("Set program")
		val editMeshes = Modal("Set meshes")

		Widget {
			text("program: ${program.id}")
			sameLine()
			button("edit##program") {
				editProgram.open(getWidget(Program::class.java, program::class.java) {
					editProgram.close()
					program = it
				})
			}
			sameLine()
			button("free") { program.close() }

			separator()

			text("meshes (${meshes.size})")
			if (meshes.isNotEmpty()) {
				sameLine()
				button("edit##meshes") {
					editMeshes.open(MeshesBuilder(meshes.first()::class.java) {
						editMeshes.close()
						setMeshes(*it.toTypedArray())
					})
				}
			}
			sameLine()
			button("free") { meshes.forEach(Mesh::close) }

			if (meshes.isNotEmpty()) {
				allMeshes(meshes)
				allMeshes.value()

				sameLine()

				list("##meshes") {
					meshes.forEach {
						text(it.id)
						tooltip {
							mesh(it)
							mesh.value()
						}
					}
				}
			}

			editProgram?.invoke()
			editMeshes?.invoke()
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? {
		TODO("Not yet implemented")
	}
}
