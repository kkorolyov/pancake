package dev.kkorolyov.pancake.input.glfw

import dev.kkorolyov.pancake.input.Compensated
import dev.kkorolyov.pancake.input.Reaction
import dev.kkorolyov.pancake.input.glfw.input.StateEvent
import dev.kkorolyov.pancake.input.glfw.input.StateEvent.State.PRESS
import dev.kkorolyov.pancake.input.glfw.input.StateEvent.State.RELEASE
import dev.kkorolyov.pancake.input.glfw.input.KeyEvent
import dev.kkorolyov.pancake.input.glfw.input.MouseButtonEvent
import dev.kkorolyov.pancake.input.glfw.input.StateEvent.State.REPEAT
import dev.kkorolyov.pancake.platform.action.Action

/**
 * Returns a reaction invoking the branch in [branches] matching an event's `key`.
 */
fun whenKey(vararg branches: Pair<Int, Reaction<in KeyEvent>>): Reaction<KeyEvent> {
	val branches = branches.toMap()

	return Reaction {
		branches[it.key]?.let { branch -> branch(it) }
	}
}

/**
 * Returns a reaction invoking the branch in [branches] matching an event's `button`.
 */
fun whenMouseButton(vararg branches: Pair<Int, Reaction<in MouseButtonEvent>>): Reaction<MouseButtonEvent> {
	val branches = branches.toMap()

	return Reaction {
		branches[it.button]?.let { branch -> branch(it) }
	}
}

/**
 * Returns a reaction returning [action] on press event.
 */
fun press(action: Action): Reaction<StateEvent> = Reaction {
	when (it.state) {
		PRESS -> action
		else -> null
	}
}
/**
 * Returns a reaction returning [action] on release event.
 */
fun release(action: Action): Reaction<StateEvent> = Reaction {
	when (it.state) {
		RELEASE -> action
		else -> null
	}
}
/**
 * Returns a reaction returning [action] on repeat event.
 */
fun repeat(action: Action): Reaction<StateEvent> = Reaction {
	when (it.state) {
		REPEAT -> action
		else -> null
	}
}

/**
 * Returns a reaction returning the main value of [compensated] on press event, and its compensating value on release event.
 */
fun toggle(compensated: Compensated<Action>): Reaction<StateEvent> = Reaction {
	when (it.state) {
		PRESS -> compensated.get()
		RELEASE -> compensated.compensate()
		else -> null
	}
}
