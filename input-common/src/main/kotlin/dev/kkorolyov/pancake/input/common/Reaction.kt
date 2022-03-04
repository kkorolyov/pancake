package dev.kkorolyov.pancake.input.common

import dev.kkorolyov.pancake.platform.action.Action

/**
 * Produces [Action]s in response to inputs.
 */
interface Reaction<T> {
	/**
	 * Potentially produces an action in response to [input].
	 */
	operator fun invoke(input: T): Action?

	companion object {
		/**
		 * Returns a reaction producing actions according to [op].
		 */
		inline operator fun <T> invoke(crossinline op: (event: T) -> Action?): Reaction<T> = object : Reaction<T> {
			override fun invoke(input: T): Action? = op(input)
		}

		/**
		 * Returns a reaction returning the first non-`null` result of invoking [delegates]; if any.
		 */
		fun <T> first(vararg delegates: Reaction<in T>): Reaction<T> = Reaction {
			delegates.asSequence()
				.firstNotNullOfOrNull { delegate -> delegate(it) }
		}

		/**
		 * Returns a reaction invoking [delegate] on events satisfying [test].
		 */
		inline fun <T> filter(crossinline test: (T) -> Boolean, delegate: Reaction<in T>): Reaction<T> = Reaction {
			if (test(it)) delegate(it) else null
		}

		/**
		 * Returns a reaction invoking [delegate] on [T] type inputs.
		 */
		inline fun <reified T> matchType(delegate: Reaction<in T>): Reaction<Any> = Reaction {
			if (it is T) delegate(it) else null
		}
	}
}
