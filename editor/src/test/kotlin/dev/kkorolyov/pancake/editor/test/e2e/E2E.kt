package dev.kkorolyov.pancake.editor.test.e2e

import dev.kkorolyov.pancake.editor.test.drawEnd
import dev.kkorolyov.pancake.editor.test.drawStart
import dev.kkorolyov.pancake.editor.test.editor
import dev.kkorolyov.pancake.editor.test.start
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.Pipeline

fun main() {
	start(GameEngine().apply {
		setPipelines(
			Pipeline.of(
				drawStart(),
				editor(this),
				drawEnd()
			)
		)
	})
}
