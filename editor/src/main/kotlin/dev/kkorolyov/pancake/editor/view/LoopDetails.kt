package dev.kkorolyov.pancake.editor.view

import dev.kkorolyov.pancake.editor.colRatios
import dev.kkorolyov.pancake.editor.controller.DataPoller
import javafx.geometry.Pos
import tornadofx.View
import tornadofx.action
import tornadofx.button
import tornadofx.gridpane
import tornadofx.gridpaneConstraints
import tornadofx.hbox
import tornadofx.imageview
import tornadofx.label
import tornadofx.listview
import tornadofx.onChange
import tornadofx.paddingLeft
import tornadofx.vbox

class LoopDetails : View() {
	private val poller: DataPoller by inject()

	private val playIcon = imageview("icons/play.png")
	private val pauseIcon = imageview("icons/pause.png")

	override val root = gridpane {
		label(poller.tps) {
			paddingLeft = 4
			gridpaneConstraints {
				columnRowIndex(0, 0)
			}
		}
		hbox {
			button(graphic = pickGraphic(poller.active.value)) {
				poller.active.onChange {
					graphic = pickGraphic(it)
				}
				action {
					poller.loop.value?.let {
						if (poller.active.value) it.stop() else it.start()
					}
				}
			}
			alignment = Pos.CENTER
			gridpaneConstraints {
				columnRowIndex(1, 0)
			}
		}

		vbox {
			label("Events")
			listview(poller.events) {
				maxHeight = 240.0
			}

			gridpaneConstraints {
				columnRowIndex(0, 1)
				columnSpan = 3
			}
		}

		colRatios(1, 8, 1)
	}

	private fun pickGraphic(active: Boolean) = if (active) pauseIcon else playIcon
}
