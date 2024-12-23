package dev.kkorolyov.pancake.editor

/**
 * Formats a value using the broadest matching order of magnitude from an ascending list of magnitudes of the form `(minValue, unit)`.
 */
class MagFormat(private vararg val units: Pair<Long, String>) {
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

	companion object {
		/**
		 * Formatter for seconds from `ns` to `s`.
		 */
		val nanos: MagFormat by lazy {
			MagFormat(
				1L to "ns",
				1e3.toLong() to "us",
				1e6.toLong() to "ms",
				1e9.toLong() to "s"
			)
		}

		/**
		 * Formatter for bytes from `B` to `GiB`.
		 */
		val bytes: MagFormat by lazy {
			MagFormat(
				1L to "B",
				1L shl 10 to "KiB",
				1L shl 20 to "MiB",
				1L shl 30 to "GiB"
			)
		}
	}
}
