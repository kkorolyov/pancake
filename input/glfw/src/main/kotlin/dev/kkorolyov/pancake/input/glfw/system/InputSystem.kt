package dev.kkorolyov.pancake.input.glfw.system

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.input.component.Input
import dev.kkorolyov.pancake.input.glfw.input.ActionEvent.Action.Companion.forValue
import dev.kkorolyov.pancake.input.glfw.input.CursorPosEvent
import dev.kkorolyov.pancake.input.glfw.input.InputEvent
import dev.kkorolyov.pancake.input.glfw.input.KeyEvent
import dev.kkorolyov.pancake.input.glfw.input.MouseButtonEvent
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import org.lwjgl.glfw.GLFW.*

/**
 * Queues actions from input.
 */
class InputSystem(vararg windows: Long) : GameSystem(Input::class.java, ActionQueue::class.java) {
	private val events = mutableListOf<InputEvent>()

	init {
		windows.forEach {
			glfwSetKeyCallback(it) { window, key, scanCode, action, mods ->
				events.add(KeyEvent(window, key, scanCode, forValue(action), mods))
			}.use { }

			glfwSetMouseButtonCallback(it) { window, button, action, mods ->
				events.add(MouseButtonEvent(window, button, forValue(action), mods))
			}.use { }

			glfwSetCursorPosCallback(it) { window, x, y ->
				events.add(CursorPosEvent(window, x, y))
			}
		}
	}

	override fun before() = glfwPollEvents()

	override fun update(entity: Entity, dt: Long) {
		val input = entity[Input::class.java]
		val actionQueue = entity[ActionQueue::class.java]
		events.forEach { input(it)?.let(actionQueue::enqueue) }
	}

	override fun after() = events.clear()
}
