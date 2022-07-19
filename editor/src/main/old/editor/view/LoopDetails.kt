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
import tornadofx.onChange
import tornadofx.onLeftClick
import tornadofx.paddingLeft
import tornadofx.stringBinding
import tornadofx.tooltip
import java.text.DecimalFormat

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

			tooltip("Average ticks per second")
		}
		hbox {
			button(graphic = pickGraphic(poller.speed.get())) {
				poller.speed.onChange {
					graphic = pickGraphic(it)
				}
				action {
					poller.engine.value?.let {
						it.speed = if (it.speed == 0.0) 1.0 else 0.0
					}
				}

				tooltip("Active state")
			}

			val format = DecimalFormat("0.0")
			label(poller.speed.stringBinding { it?.let(format::format) }) {
				setOnScroll { e ->
					poller.engine.value?.let {
						it.speed += if (e.deltaY >= 0) 0.1 else -0.1
					}
				}
				onLeftClick {
					poller.engine.value?.let {
						it.speed = 1.0
					}
				}

				tooltip("Scale. Scroll to change. Click to reset.")

				alignment = Pos.CENTER
				prefWidth = 32.0
			}

			alignment = Pos.CENTER
			gridpaneConstraints {
				columnRowIndex(1, 0)
			}
		}

		colRatios(1, 8, 1)
	}

	private fun pickGraphic(speed: Double) = if (speed == 0.0) playIcon else pauseIcon
}
