package dev.kkorolyov.killstreek.component

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.BoundedValue

/**
 * Maintains an entity's state of existence.
 * @param max maximum value for health, constrained {@code > 0}
 * @param current initial health, constrained {@code <= max}
 */
class Health(max: Int, current: Int = max) : Component {
	/** health value */
	val value: BoundedValue<Int> = BoundedValue<Int>(null, max, current)

	/** percentage of current health with respect to max health */
	val percentage: Double get() = value.get().toDouble() / value.maximum.toDouble()

	/** whether current health {@code <= 0} */
	val isDead: Boolean get() = value.get() <= 0
	/** whether current health {@code < 0} */
	val isSuperDead: Boolean get() = value.get() < 0

	/**
	 * Applies a change to current health.
	 * @param amount change in current health
	 */
	fun change(amount: Int) {
		value.set(value.get() + amount)
	}
}
