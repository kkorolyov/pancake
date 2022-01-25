package dev.kkorolyov.pancake.editor.view

import dev.kkorolyov.pancake.editor.controller.DataSelection
import dev.kkorolyov.pancake.editor.controller.DataPoller
import dev.kkorolyov.pancake.editor.data.EntityData
import dev.kkorolyov.pancake.editor.getComponentDetails
import javafx.collections.FXCollections
import tornadofx.View
import tornadofx.column
import tornadofx.field
import tornadofx.fieldset
import tornadofx.form
import tornadofx.integerBinding
import tornadofx.label
import tornadofx.onChange
import tornadofx.onSelectionChange
import tornadofx.select
import tornadofx.tab
import tornadofx.tableview
import tornadofx.tabpane
import java.util.Comparator.comparing

class EntitiesTable : View() {
	private val poller: DataPoller by inject()
	private val dataSelection: DataSelection by inject()

	override val root = tableview(poller.entities) {
		column<EntityData, Int>("ID") { it.value.id }
		column<EntityData, Number>("Components") { integerBinding(it.value.components) { size } }

		onSelectionChange(dataSelection.entityData::bind)
	}
}

class EntityDetails : View() {
	private val dataSelection: DataSelection by inject()

	override val root = form {
		fieldset {
			field("ID") {
				label(dataSelection.entityData.id)
			}

			tabpane {
				dataSelection.entityData.components.onChange { change ->
					tabs.clear()
					change.list.forEach {
						tab(it.name.value) {
							add(getComponentDetails(it))
						}
					}
					FXCollections.sort(tabs, comparing { it.text })
					tabs.firstOrNull()?.select()
				}
			}
		}
	}
}
