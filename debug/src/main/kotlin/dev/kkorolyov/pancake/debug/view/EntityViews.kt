package dev.kkorolyov.pancake.debug.view

import dev.kkorolyov.pancake.debug.controller.DataDetails
import dev.kkorolyov.pancake.debug.controller.EnginePoller
import dev.kkorolyov.pancake.debug.data.EntityData
import dev.kkorolyov.pancake.debug.getComponentDetails
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
	private val poller: EnginePoller by inject()
	private val dataDetails: DataDetails by inject()

	override val root = tableview(poller.entities) {
		column<EntityData, Int>("ID") { it.value.id }
		column<EntityData, Number>("Components") { integerBinding(it.value.components) { size } }

		onSelectionChange(dataDetails.entityData::bind)
	}
}

class EntityDetails : View() {
	private val dataDetails: DataDetails by inject()

	override val root = form {
		fieldset {
			field("ID") {
				label(dataDetails.entityData.id)
			}

			tabpane {
				dataDetails.entityData.components.onChange { change ->
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
