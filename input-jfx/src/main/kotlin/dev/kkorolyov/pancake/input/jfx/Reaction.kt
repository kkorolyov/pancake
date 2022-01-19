package dev.kkorolyov.pancake.input.jfx

import dev.kkorolyov.pancake.platform.action.Action
import javafx.scene.input.InputEvent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent

/**
 * Reacts to an input event.
 */
interface Reaction<T : InputEvent> {
	/**
	 * Potentially provides an action to an input [event].
	 */
	operator fun invoke(event: T): Action?

	companion object {
		inline operator fun <T : InputEvent> invoke(crossinline op: (event: T) -> Action?) = object : Reaction<T> {
			override fun invoke(event: T): Action? = op(event)
		}

		/**
		 * Returns a reaction returning the first non-`null` result of invoking [delegates]; if any.
		 */
		fun <T : InputEvent> first(vararg delegates: Reaction<in T>): Reaction<T> = object : Reaction<T> {
			override fun invoke(event: T): Action? = delegates.asSequence()
				.firstNotNullOfOrNull { it(event) }
		}

		/**
		 * Returns a reaction invoking [delegate] on events satisfying [test].
		 */
		fun <T : InputEvent> filter(test: (T) -> Boolean, delegate: Reaction<in T>): Reaction<T> = object : Reaction<T> {
			override fun invoke(event: T): Action? = if (test(event)) delegate(event) else null
		}

		/**
		 * Returns a reaction invoking the reaction of the first branch of [branches] matching an event's [KeyEvent.code].
		 */
		fun whenCode(vararg branches: Pair<KeyCode, Reaction<in KeyEvent>>): Reaction<KeyEvent> =
			object : Reaction<KeyEvent> {
				override fun invoke(event: KeyEvent): Action? = branches.asSequence()
					.filter { it.first == event.code }
					.map { it.second(event) }
					.firstOrNull()
			}

		/**
		 * Returns a reaction invoking [delegate] on [T] type events.
		 */
		inline fun <reified T : InputEvent> matchType(delegate: Reaction<in T>): Reaction<InputEvent> =
			object : Reaction<InputEvent> {
				override fun invoke(event: InputEvent): Action? = if (event is T) delegate(event) else null
			}

		/**
		 * Returns a reaction returning the main value of [compensated] on press event, and its compensating value on release event.
		 */
		fun keyToggle(compensated: Compensated<Action>): Reaction<KeyEvent> = object : Reaction<KeyEvent> {
			override fun invoke(event: KeyEvent): Action? {
				return when (event.eventType) {
					KeyEvent.KEY_PRESSED -> compensated.get()
					KeyEvent.KEY_RELEASED -> compensated.compensate()
					else -> null
				}
			}
		}

		/**
		 * Returns a reaction returning the main value of [compensated] on press event, and its compensating value on release event.
		 */
		fun mouseToggle(compensated: Compensated<Action>): Reaction<MouseEvent> = object : Reaction<MouseEvent> {
			override fun invoke(event: MouseEvent): Action? = when (event.eventType) {
				MouseEvent.MOUSE_PRESSED -> compensated.get()
				MouseEvent.MOUSE_RELEASED -> compensated.compensate()
				else -> null
			}
		}
	}
}
