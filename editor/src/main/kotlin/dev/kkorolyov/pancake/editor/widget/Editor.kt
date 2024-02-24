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

	/**
	 * Focuses (creating if needed) the window for [system].
	 */
	fun focus(system: GameSystem, openAt: OpenAt? = OpenAt.CurrentWindow) {
		systemManifest[system] = { Window(system.debugName, withDropHandlers(getWidget(GameSystem::class.java, system)), size = windowMin(3.0), openAt = openAt) }
	}
	/**
	 * Focuses (creating if needed) the window for [entity].
	 */
	fun focus(entity: Entity, openAt: OpenAt? = OpenAt.CurrentWindow) {
		entityManifest[entity] = { Window("${entity.debugName} (${entity.id})", withDropHandlers(EntityDetails(entity, componentDragDropId)), size = windowMin(2.0), openAt = openAt) }
	}
	/**
	 * Focuses (creating if needed) the window for [ownedComponent].
	 */
	fun focus(ownedComponent: OwnedComponent, openAt: OpenAt? = OpenAt.CurrentWindow) {
		val (entity, component) = ownedComponent
		componentManifest[ownedComponent] = { Window("${entity.debugName} - ${component.debugName}", withDropHandlers(getWidget(Component::class.java, component)), size = windowMin(), openAt = openAt) }
	}

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
			useDragDropPayload<GameSystem>(systemDragDropId) { focus(it, OpenAt.Cursor) }
			useDragDropPayload<Entity>(entityDragDropId) { focus(it, OpenAt.Cursor) }
			useDragDropPayload<OwnedComponent>(componentDragDropId) { focus(it, OpenAt.Cursor) }
		}
	}

	private fun windowMin(scale: Double = 1.0) = Vector2.of(128 * scale, 128 * scale)
}

