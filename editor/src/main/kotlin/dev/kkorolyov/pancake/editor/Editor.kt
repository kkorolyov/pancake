package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.editor.controller.DataPoller
import dev.kkorolyov.pancake.editor.view.EditorScene
import dev.kkorolyov.pancake.platform.GameLoop
import javafx.stage.Stage
import tornadofx.find

/**
 * Registers the global editor state with [loop].
 */
fun registerEditor(loop: GameLoop) {
	find<DataPoller>().register(loop)
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
