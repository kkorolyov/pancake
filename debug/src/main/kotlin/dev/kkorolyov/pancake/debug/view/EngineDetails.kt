package dev.kkorolyov.pancake.debug.view

import dev.kkorolyov.pancake.debug.controller.EnginePoller
import tornadofx.View
import tornadofx.hbox
import tornadofx.label

class EngineDetails : View() {
	private val poller: EnginePoller by inject()

	override val root = hbox {
		label(poller.tps)
	}
}
