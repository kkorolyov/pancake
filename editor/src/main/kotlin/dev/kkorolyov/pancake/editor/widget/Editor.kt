package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.header
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
	private val loopDetails by lazy { LoopDetails(engine) }
	private val pipelines by lazy { PipelinesTree(engine.toList()) }
	private val entities by lazy { EntitiesTable(engine.entities) }

	override fun invoke() {
		header("Loop Details") {
			loopDetails()
		}
		header("Pipelines") {
			pipelines()
		}
		header("Entities") {
			entities()
		}
	}
}

