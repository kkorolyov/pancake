package e2e

import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.Pipeline

fun main() {
	start(GameEngine().apply {
		setPipelines(
			Pipeline(
				drawStart(),
				editor(this),
				drawEnd()
			)
		)
	})
}
