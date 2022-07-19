package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.editor.controller.DataPoller
import dev.kkorolyov.pancake.editor.view.EditorScene
import dev.kkorolyov.pancake.platform.GameEngine
import javafx.stage.Stage
import tornadofx.find

/**
 * Registers the global editor state with [engine].
 */
fun registerEditor(engine: GameEngine) {
	find<DataPoller>().register(engine)
}

/**
 * Opens editor GUI in a new window.
 * Editor window is opened next to [stage], if non-`null`.
 */
fun openEditor(stage: Stage? = null) {
	find<EditorScene>().openWindow()?.let { otherStage ->
		stage?.let {
			otherStage.x = it.x + it.width
			otherStage.y = it.y
		}
	}
}
