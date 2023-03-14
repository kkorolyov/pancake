package dev.kkorolyov.pancake.editor.core

import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.editor.ComponentWidgetFactory
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.readonly
import dev.kkorolyov.pancake.editor.group
import dev.kkorolyov.pancake.editor.indented
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component

class BoundsComponentWidgetFactory : ComponentWidgetFactory {
	override fun get(component: Component): Widget? = ComponentWidgetFactory.get<Bounds>(component) {
		group {
			text("Vertices")
			indented {
				list("##vertices") {
					vertices.forEach { it.readonly("##vertices") }
				}
			}
		}
		group {
			text("Normals")
			indented {
				list("##normals") {
					normals.forEach { it.readonly("##normals") }
				}
			}
		}
		group {
			text("Magnitude")
			indented {
				text(magnitude)
			}
		}
	}
}
