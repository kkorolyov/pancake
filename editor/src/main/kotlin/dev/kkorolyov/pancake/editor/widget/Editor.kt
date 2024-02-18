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
import dev.kkorolyov.pancake.platform.math.Vector2
import imgui.flag.ImGuiDir
import imgui.type.ImBoolean

private val windowMin = Vector2.of(128.0, 128.0)

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
	private val pipelines = Window("Pipelines", withDropHandlers(PipelinesList(engine, systemDragDropId)), subVisible)
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
				systemManifest[it] = { Window(it.debugName, withDropHandlers(getWidget(GameSystem::class.java, it)), size = windowMin, openAt = OpenAt.Cursor) }
			}
			useDragDropPayload<Entity>(entityDragDropId) {
				entityManifest[it] = { Window("Entity ${it.id}", withDropHandlers(EntityDetails(it, componentDragDropId)), size = windowMin, openAt = OpenAt.Cursor) }
			}
			useDragDropPayload<OwnedComponent>(componentDragDropId) {
				val (entity, component) = it
				componentManifest[it] = { Window("${entity.id}.${component.debugName}", withDropHandlers(getWidget(Component::class.java, component)), size = windowMin, openAt = OpenAt.Cursor) }
			}
		}
	}
}

