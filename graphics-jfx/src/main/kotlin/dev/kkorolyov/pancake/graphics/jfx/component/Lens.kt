package dev.kkorolyov.pancake.graphics.jfx.component

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vectors
import dev.kkorolyov.pancake.platform.utility.ArgVerify
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import java.util.*

/**
 * A view on some set of drawn layers at some unit:pixel scaling factor.
 */
class Lens(
	/**
	 * Canvas on which to draw seen things.
	 */
	val canvas: Canvas,
	/**
	 * Maps drawn units to pixel lengths.
	 * All components must be `> 0`.
	 * e.g. at a scale of `32x32`, a `2x1` unit rectangular shape is drawn as `64x32` pixels.
	 */
	scale: Vector2,
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
	/** Maps drawn units to pixel lengths. */
	val scale: Vector2 = Vectors.create(
		ArgVerify.greaterThan("scale.x", 0.0, scale.x),
		ArgVerify.greaterThan("scale.y", 0.0, scale.y)
	)

}
