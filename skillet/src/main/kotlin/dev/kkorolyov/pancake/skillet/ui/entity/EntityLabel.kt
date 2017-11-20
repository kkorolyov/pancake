package dev.kkorolyov.pancake.skillet.ui.entity

import dev.kkorolyov.pancake.skillet.decorator.*
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.GenericEntity.EntityChangeEvent
import dev.kkorolyov.pancake.skillet.model.Model.ModelChangeEvent
import dev.kkorolyov.pancake.skillet.model.ModelListener
import dev.kkorolyov.pancake.skillet.ui.Panel
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.layout.VBox

/**
 * Editable label displaying an entity name.
 */
class EntityLabel(entity: GenericEntity) : Panel, ModelListener<GenericEntity> {
	private val label: Label = Label(entity.name)
			.styleClass("entity-name")
			.minSize(10.0)
			.click(2) {
				textField.text = entity.name
				swapTo(textField)
				textField.requestFocus()
			}
	private val textField: TextField = TextField()
			.change(Node::focusedProperty) { target, _, newValue ->
				if (!newValue) {
					entity.name = target.text
					swapTo(label)
				}
			}
			.press(mapOf(
					KeyCodeCombination(KeyCode.ENTER) to { target ->
						entity.name = target.text
						swapTo(label)
					},
					KeyCodeCombination(KeyCode.ESCAPE) to { _ ->
						swapTo(label)
					}
			))
	override val root: VBox = VBox(label)
			.styleClass("entity-name")

	init {
		entity.register(this)
	}

	private fun swapTo(node: Node) {
		root.children.clear()
		root.children += node
	}

	override fun changed(target: GenericEntity, event: ModelChangeEvent) {
		when(event) {
			EntityChangeEvent.NAME -> { label.text = target.name }
		}
	}
}
