package dev.kkorolyov.pancake.input.glfw

import dev.kkorolyov.pancake.input.common.Compensated
import dev.kkorolyov.pancake.input.common.Reaction
import dev.kkorolyov.pancake.input.glfw.input.ActionEvent
import dev.kkorolyov.pancake.input.glfw.input.ActionEvent.Action.PRESS
import dev.kkorolyov.pancake.input.glfw.input.ActionEvent.Action.RELEASE
import dev.kkorolyov.pancake.input.glfw.input.KeyEvent
import dev.kkorolyov.pancake.input.glfw.input.MouseButtonEvent
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
 * Returns a reaction returning the main value of [compensated] on press event, and its compensating value on release event.
 */
fun toggle(compensated: Compensated<Action>): Reaction<ActionEvent> = Reaction {
	when (it.action) {
		PRESS -> compensated.get()
		RELEASE -> compensated.compensate()
		else -> null
	}
}
