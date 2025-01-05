package dev.kkorolyov.pancake.input.glfw.system

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.input.component.InputMapper
import dev.kkorolyov.pancake.input.event.CursorPosEvent
import dev.kkorolyov.pancake.input.event.InputEvent
import dev.kkorolyov.pancake.input.event.KeyEvent
import dev.kkorolyov.pancake.input.event.MouseButtonEvent
import dev.kkorolyov.pancake.input.event.StateEvent
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import org.lwjgl.glfw.GLFW.*

/**
 * Queues actions from input.
 */
class InputSystem(vararg windows: Long) : GameSystem(InputMapper::class.java, ActionQueue::class.java) {
	private val events = mutableListOf<InputEvent>()

	init {
		windows.forEach {
			val currKeyCallback = glfwSetKeyCallback(it, null)
			glfwSetKeyCallback(it) { window, key, scanCode, action, mods ->
				currKeyCallback?.invoke(window, key, scanCode, action, mods)
				events.add(KeyEvent(key, scanCode, StateEvent.State.entries[action], mods))
			}

			val currMouseButtonCallback = glfwSetMouseButtonCallback(it, null)
			glfwSetMouseButtonCallback(it) { window, button, action, mods ->
				currMouseButtonCallback?.invoke(window, button, action, mods)
				events.add(MouseButtonEvent(button, StateEvent.State.entries[action], mods))
			}

			val currCursorPosCallback = glfwSetCursorPosCallback(it, null)
			glfwSetCursorPosCallback(it) { window, x, y ->
				currCursorPosCallback?.invoke(window, x, y)
				events.add(CursorPosEvent(x, y))
			}
		}
	}

	override fun before(dt: Long) = glfwPollEvents()

	override fun update(entity: Entity, dt: Long) {
		val inputMapper = entity[InputMapper::class.java]
		val actionQueue = entity[ActionQueue::class.java]
		events.forEach { inputMapper(it)?.let(actionQueue::add) }
	}

	override fun after(dt: Long) = events.clear()
}
