package dev.kkorolyov.pancake.input.jfx.system

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.input.common.component.Input
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.InputEvent

/**
 * Queues actions based on input.
 */
class InputSystem(vararg inputNodes: Node) : GameSystem(Input::class.java, ActionQueue::class.java) {
	private val events = IsolateCollection<InputEvent>()

	init {
		val handler = EventHandler { event: InputEvent -> events += event }

		inputNodes.forEach {
			it.onKeyPressed = handler
			it.onKeyReleased = handler

			it.onMousePressed = handler
			it.onMouseReleased = handler

			it.onMouseMoved = handler
		}
	}

	override fun before() {
		events.swap()
	}

	override fun update(entity: Entity, dt: Long) {
		val input = entity.get(Input::class.java)
		val actionQueue = entity.get(ActionQueue::class.java)
		events.forEach {
			input(it)?.let(actionQueue::enqueue)
		}
	}

	override fun after() {
		events.clear()
	}

	/**
	 * Isolates read and written partitions, and provides for swapping them.
	 */
	private class IsolateCollection<T> : Iterable<T> {
		private var writer = mutableListOf<T>()
		private var reader = mutableListOf<T>()

		@Synchronized
		fun swap() {
			val temp = writer
			writer = reader
			reader = temp
		}

		@Synchronized
		operator fun plusAssign(t: T) {
			writer += t
		}

		fun clear() {
			reader.clear()
		}

		override fun iterator(): Iterator<T> = reader.iterator()
	}
}
