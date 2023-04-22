package dev.kkorolyov.pancake.graphics.editor

import dev.kkorolyov.pancake.editor.MemoizedContent
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.propertiesTable
import dev.kkorolyov.pancake.editor.propertyRow
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.graphics.component.Model
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.platform.entity.Component

class ModelComponentWidgetFactory : WidgetFactory<Component> {
	override val type = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Model>(t) {
		val mesh = MemoizedContent<Mesh>({ getWidget(Mesh::class.java, it) })

		Widget {
			propertiesTable("model") {
				propertyRow("program") {
					selectable(program.id.toString()) { program.close() }
					tooltip("Click to free")
				}
				propertyRow("meshes") {
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
		}
	}

	override fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget? {
		TODO("Not yet implemented")
	}
}
