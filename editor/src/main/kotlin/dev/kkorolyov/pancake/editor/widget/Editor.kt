package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.child
import dev.kkorolyov.pancake.editor.data.OwnedComponent
import dev.kkorolyov.pancake.editor.dockSpace
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.onDrop
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import imgui.flag.ImGuiDir
import imgui.type.ImBoolean

/**
 * Renders the overall editor view for a [GameEngine].
 */
class Editor(
	/**
	 * Associated engine.
	 */
	engine: GameEngine,
) : Widget {
	private val subVisible = ImBoolean(true)

	private val systemDragDropId = "selectedSystem"
	private val entityDragDropId = "selectedEntity"
	private val componentDragDropId = "selectedComponent"

	private val systemManifest = WindowManifest<GameSystem>()
	private val entityManifest = WindowManifest<Entity>()
	private val componentManifest = WindowManifest<OwnedComponent>()

	private val loop = Window("GameLoop", withDropHandlers(LoopDetails(engine)), subVisible)
	private val pipelines = Window("Pipelines", withDropHandlers(PipelinesTree(engine, systemDragDropId)), subVisible)
	private val entities = Window("Entities", withDropHandlers(EntitiesTable(engine.entities, entityDragDropId)), subVisible)
	private val gl = Window("OpenGL", withDropHandlers(GLDetails()), subVisible)

	override fun invoke() {
		if (!subVisible.get()) subVisible.set(true)

		withDropHandlers {
			dockSpace("dock") {
				dock("dock", entities)
				dock(entities, loop, ImGuiDir.Up, 0.2f)
				dock(entities, pipelines, ImGuiDir.Left, 0.2f)
				dock(loop, gl, ImGuiDir.Right, 0.5f)
			}
		}

		loop()
		pipelines()
		entities()
		gl()

		systemManifest()
		entityManifest()
		componentManifest()
	}

	private fun withDropHandlers(widget: Widget): Widget = Widget {
		withDropHandlers { widget() }
	}

	private inline fun withDropHandlers(op: () -> Unit) {
		child("dropSpace") {
			op()
		}
		drawDropHandlers()
	}

	private fun drawDropHandlers() {
		onDrop {
			useDragDropPayload<GameSystem>(systemDragDropId) {
				systemManifest[it] = { Window(it::class.simpleName ?: "GameSystem", withDropHandlers(getWidget(GameSystem::class.java, it)), openAt = OpenAt.Cursor) }
			}
			useDragDropPayload<Entity>(entityDragDropId) {
				entityManifest[it] = { Window("Entity ${it.id}", withDropHandlers(EntityDetails(it, componentDragDropId)), openAt = OpenAt.Cursor) }
			}
			useDragDropPayload<OwnedComponent>(componentDragDropId) {
				val (entity, component) = it
				componentManifest[it] = { Window("${entity.id}.${component::class.simpleName}", withDropHandlers(getWidget(Component::class.java, component)), openAt = OpenAt.Cursor) }
			}
		}
	}
}

