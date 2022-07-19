package dev.kkorolyov.pancake.editor.view

import dev.kkorolyov.pancake.editor.ComponentData
import dev.kkorolyov.pancake.editor.ComponentDetails
import tornadofx.field
import tornadofx.fieldset
import tornadofx.form
import tornadofx.label

class BasicComponentDetails(private val model: ComponentData<*, *>) : ComponentDetails() {
	override val root = form {
		fieldset {
			field("Value") {
				label(model.stringValue)
			}
		}
	}
}
