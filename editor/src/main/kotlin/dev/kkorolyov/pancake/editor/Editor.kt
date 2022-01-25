package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.editor.controller.DataPoller
import dev.kkorolyov.pancake.editor.view.EditorScene
import dev.kkorolyov.pancake.platform.GameLoop
import javafx.stage.Stage
import tornadofx.find

fun registerEditor(loop: GameLoop) {
	find<DataPoller>().register(loop)
}

fun openEditor(stage: Stage) {
	find<EditorScene>().openWindow()?.let { otherStage ->
		otherStage.x = stage.x + stage.width
		otherStage.y = stage.y
	}
}
