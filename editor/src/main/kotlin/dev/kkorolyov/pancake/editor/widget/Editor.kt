package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.header
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.GameSystem

/**
 * Renders the overall editor view for a [GameEngine].
 */
class Editor(
	/**
	 * Associated engine.
	 */
	engine: GameEngine,
) : Widget {
	private val systemManifest = WindowManifest<GameSystem>()

	private val loopDetails by lazy { LoopDetails(engine) }
	private val pipelines by lazy { PipelinesTree(engine.toList(), systemManifest) }
	private val entities by lazy { EntitiesTable(engine.entities) }
	private val gl by lazy { GLDetails() }

	override fun invoke() {
		loopDetails()
		header("Pipelines") {
			pipelines()
		}
		header("Entities") {
			entities()
		}
		header("OpenGL") {
			gl()
		}

		systemManifest()
	}
}

