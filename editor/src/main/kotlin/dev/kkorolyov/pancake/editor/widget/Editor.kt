package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.header
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity

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
	private val entityManifest = WindowManifest<Entity>()
	private val componentManifest = WindowManifest<Component>()

	private val loopDetails by lazy { LoopDetails(engine) }
	private val pipelines by lazy { PipelinesTree(engine.toList(), systemManifest) }
	private val entities by lazy { EntitiesTable(engine.entities, entityManifest, componentManifest) }
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
		entityManifest()
		componentManifest()
	}
}

