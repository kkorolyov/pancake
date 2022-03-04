package dev.kkorolyov.pancake.input.glfw

import dev.kkorolyov.pancake.input.common.Reaction
import dev.kkorolyov.pancake.input.glfw.input.KeyEvent
import dev.kkorolyov.pancake.input.glfw.input.MouseButtonEvent

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
