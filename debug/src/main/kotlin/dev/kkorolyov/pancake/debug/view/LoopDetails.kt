package dev.kkorolyov.pancake.debug.view

import dev.kkorolyov.pancake.debug.controller.DataPoller
import tornadofx.View
import tornadofx.action
import tornadofx.borderpane
import tornadofx.button
import tornadofx.center
import tornadofx.hbox
import tornadofx.imageview
import tornadofx.label
import tornadofx.left
import tornadofx.onChange

class LoopDetails : View() {
	private val poller: DataPoller by inject()

	private val playIcon = imageview("icons/play.png")
	private val pauseIcon = imageview("icons/pause.png")

	override val root = borderpane {
		left {
			label(poller.tps)
		}
		center {
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
		}
	}

	private fun pickGraphic(active: Boolean) = if (active) pauseIcon else playIcon
}
