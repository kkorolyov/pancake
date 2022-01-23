package dev.kkorolyov.pancake.debug.view

import dev.kkorolyov.pancake.debug.controller.DataDetails
import dev.kkorolyov.pancake.debug.controller.EnginePoller
import dev.kkorolyov.pancake.debug.data.GameSystemData
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
	private val poller: EnginePoller by inject()
	private val dataDetails: DataDetails by inject()

	override val root = tableview(poller.systems) {
		column<GameSystemData, String>("System") { it.value.name }
		column<GameSystemData, String>("Signature") { it.value.signature }
		column<GameSystemData, Long>("Tick time (ns)") { it.value.tick }
		column<GameSystemData, Int?>("TPS") { it.value.tps }

		columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY

		onSelectionChange(dataDetails.gameSystemData::bind)
	}
}

class SystemDetails : View() {
	private val dataDetails: DataDetails by inject()

	override val root = form {
		fieldset {
			field("Name") {
				label(dataDetails.gameSystemData.name)
			}
			field("Signature") {
				label(dataDetails.gameSystemData.signature)
			}
			field("Tick") {
				label(dataDetails.gameSystemData.tick)
			}
			field("TPS") {
				label(dataDetails.gameSystemData.tps)
			}
		}
	}
}
