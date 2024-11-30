package dev.kkorolyov.pancake.graphics.component

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.utility.ArgVerify
import java.util.BitSet

/**
 * A view on some set of drawn layers at some pixel:unit scaling factor.
 */
// TODO implement view frustum for 3D support - way down the line
class Lens(
	/**
	 * Maps drawn units to pixel lengths.
	 * All components must be `> 0`.
	 * e.g. at a scale of `32x32`, a `2x1` unit rectangular shape is drawn as `64x32` pixels.
	 */
	scale: Vector2,
	/**
	 * Width and height of the target render medium in `px`.
	 * All components must be `> 0`.
	 */
	size: Vector2,
	/**
	 * Offset of the target render medium in `px`.
	 */
	offset: Vector2 = Vector2.of(0.0, 0.0),
	/**
	 * Denotes which layers are rendered from the perspective of this lens.
	 * e.g. a mask of `1011` renders drawable elements found on layers `0`, `2`, and `3`.
	 * Defaults to a 1-bit mask set to `true`.
	 */
	val mask: BitSet = BitSet(1).apply { set(0) },
	/**
	 * Whether this lens should be treated as currently rendering.
	 * Defaults to `true`.
	 */
	var active: Boolean = true
) : Component {
	/**
	 * Maps drawn units to pixel lengths.
	 */
	val scale: Vector2 = Vector2.of(
		ArgVerify.greaterThan("scale.x", 0.0, scale.x),
		ArgVerify.greaterThan("scale.y", 0.0, scale.y)
	)

	/**
	 * Width and height of the target render medium in `px`.
	 */
	val size: Vector2 = Vector2.of(
		ArgVerify.greaterThan("size.x", 0.0, size.x),
		ArgVerify.greaterThan("size.y", 0.0, size.y)
	)

	/**
	 * Offset of the target render medium in `px`.
	 */
	val offset: Vector2 = Vector2.of(offset)

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Lens) return false

		if (active != other.active) return false
		if (mask != other.mask) return false
		if (scale != other.scale) return false
		if (size != other.size) return false
		if (offset != other.offset) return false

		return true
	}

	override fun hashCode(): Int {
		var result = active.hashCode()
		result = 31 * result + mask.hashCode()
		result = 31 * result + scale.hashCode()
		result = 31 * result + size.hashCode()
		result = 31 * result + offset.hashCode()
		return result
	}
}
