package dev.kkorolyov.pancake.debug

import dev.kkorolyov.pancake.debug.controller.EnginePoller
import dev.kkorolyov.pancake.debug.view.DebugScene
import dev.kkorolyov.pancake.platform.Config
import dev.kkorolyov.pancake.platform.GameEngine
import javafx.stage.Stage
import tornadofx.find

fun registerDebug(engine: GameEngine) {
	find<EnginePoller>().register(engine)
}

fun openDebug(stage: Stage) {
	find<DebugScene>().openWindow()?.let { otherStage ->
		otherStage.x = stage.x + stage.width
		otherStage.y = stage.y
	}
}
