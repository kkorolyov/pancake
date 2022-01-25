package dev.kkorolyov.pancake.debug

import dev.kkorolyov.pancake.debug.controller.DataPoller
import dev.kkorolyov.pancake.debug.view.DebugScene
import dev.kkorolyov.pancake.platform.GameLoop
import javafx.stage.Stage
import tornadofx.find

fun registerDebug(loop: GameLoop) {
	find<DataPoller>().register(loop)
}

fun openDebug(stage: Stage) {
	find<DebugScene>().openWindow()?.let { otherStage ->
		otherStage.x = stage.x + stage.width
		otherStage.y = stage.y
	}
}
