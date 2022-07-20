package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.header
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.GameEngine

/**
 * Renders the overall editor view for a [GameEngine].
 */
class Editor(
	/**
	 * Associated engine.
	 */
	engine: GameEngine,
) : Widget {
	private val loopDetails = LoopDetails(engine)
	private val systems = SystemsTable(engine.perfMonitor)
	private val entities = EntitiesTable(engine.entities)

	override fun invoke() {
		header("Loop Details") {
			loopDetails()
		}
		header("Systems") {
			systems()
		}
		header("Entities") {
			entities()
		}
	}
}

