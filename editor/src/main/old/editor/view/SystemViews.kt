package dev.kkorolyov.pancake.editor.view

import dev.kkorolyov.pancake.editor.controller.DataSelection
import dev.kkorolyov.pancake.editor.controller.DataPoller
import dev.kkorolyov.pancake.editor.data.GameSystemData
import javafx.scene.control.TableView
import tornadofx.View
import tornadofx.column
import tornadofx.field
import tornadofx.fieldset
import tornadofx.form
import tornadofx.label
import tornadofx.onSelectionChange
import tornadofx.tableview

class SystemsTable : View() {
	private val poller: DataPoller by inject()
	private val dataSelection: DataSelection by inject()

	override val root = tableview(poller.systems) {
		column<GameSystemData, String>("System") { it.value.name }
		column<GameSystemData, String>("Signature") { it.value.signature }
		column<GameSystemData, Long>("Tick time (ns)") { it.value.tick }
		column<GameSystemData, Int?>("TPS") { it.value.tps }

		columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY

		onSelectionChange(dataSelection.gameSystemData::bind)
	}
}

class SystemDetails : View() {
	private val dataSelection: DataSelection by inject()

	override val root = form {
		fieldset {
			field("Name") {
				label(dataSelection.gameSystemData.name)
			}
			field("Signature") {
				label(dataSelection.gameSystemData.signature)
			}
			field("Tick") {
				label(dataSelection.gameSystemData.tick)
			}
			field("TPS") {
				label(dataSelection.gameSystemData.tps)
			}
		}
	}
}
