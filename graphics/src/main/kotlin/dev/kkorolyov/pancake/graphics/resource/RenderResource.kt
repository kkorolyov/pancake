package dev.kkorolyov.pancake.graphics.resource

/**
 * Mappable to a cached representation on a render backend.
 * Has a [version] property that changes along with the underlying data, which can be checked for quick cache invalidation by render backends.
 */
abstract class RenderResource {
	private var known = false
	private var _version = 0
	/**
	 * An opaque int representation of the current state.
	 * Only guarantee is that when the backing state changes, this changes as well.
	 */
	val version: Int
		get() {
			known = true
			return _version
		}

	/**
	 * Updates the version.
	 * Expected to be invoked any time the backing state is changed.
	 */
	protected open fun invalidate() {
		if (known) {
			_version = if (_version == Int.MAX_VALUE) Int.MIN_VALUE else _version + 1
			known = false
		}
	}
}
