package dev.kkorolyov.pancake.debug.view

import dev.kkorolyov.pancake.debug.ComponentData
import dev.kkorolyov.pancake.debug.ComponentDetails
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
