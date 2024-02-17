package dev.kkorolyov.pancake.editor

/**
 * Formats a value using the broadest matching order of magnitude from an ascending list of magnitudes of the form `(minValue, unit)`.
 */
class MagFormat(vararg val units: Pair<Long, String>) {
	/**
	 * Formats [value] with the greatest order of magnitude less than [value], up to [precision] decimal points.
	 */
	operator fun invoke(value: Int, precision: Int = 2): String = invoke(value.toLong(), precision)
	/**
	 * Formats [value] with the greatest order of magnitude less than [value], up to [precision] decimal points.
	 */
	operator fun invoke(value: Long, precision: Int = 2): String {
		val (minVal, unit) = (units.lastOrNull { (minVal, _) -> value > minVal } ?: units.first())
		return "%.${precision}f%s".format(value.toDouble() / minVal, unit)
	}
}
