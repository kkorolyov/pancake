package dev.kkorolyov.killstreek.component

import dev.kkorolyov.pancake.platform.entity.Component

/**
 * Applies damage values to an entity.
 */
class Damage(
		/** damage value */
		value: Int,
		/** {@code ns} over which to distribute applied damage, values close to (but not at) {@code 0} result in effectively immediate damage application */
		duration: Long = 1
) : Component {
	/** damage value */
	val value get() = _value
	/** {@code ns} over which damage distributed */
	val duration get() = _duration

	/** whether subsequent damage applications will do nothing */
	val expired: Boolean get() = elapsed >= duration

	private var _value: Int = 0
	private var effectiveValue: Double = 0.0
	private var remaining: Int = 0

	private var _duration: Long = 0
	private var elapsed: Long = 0

	init {
		setValue(value, duration)
	}

	/**
	 * Constructs a new, expired damage.
	 */
	constructor() : this(0, 0)

	/**
	 * Applies the negation of this damage's value to some health.
	 * If this damage has a duration, the appropriate portion of its value is applied instead.
	 * If this damage has expired, this method does nothing.
	 * @param health damaged health
	 * @param dt {@code ns} elapsed since previous invocation of this method
	 * @return {@code false} if expired, else {@code true}
	 */
	fun apply(health: Health, dt: Long): Boolean {
		if (expired) return false
		elapsed += dt

		effectiveValue += (if (expired) remaining.toDouble() else value * (Math.min(dt.toDouble() / _duration, 1.0)))
		if (Math.abs(effectiveValue) >= 1.0) {
			val removed = -effectiveValue.toInt()

			remaining += removed

			health.change(removed)
			effectiveValue = 0.0
		}
		return true
	}

	/**
	 * Resets elapsed damage duration.
	 */
	fun reset() {
		setValue(value, duration)
	}

	/**
	 * @param value new damage value
	 * @param duration {@code ns} over which to distribute applied damage, values close to (but not at) {@code 0} result in effectively immediate damage application, constrained {@code >= 0}
	 */
	fun setValue(value: Int, duration: Long = 1) {
		_value = value
		effectiveValue = 0.0
		remaining = value

		_duration = duration
		elapsed = Math.min(0, duration)
	}
}
