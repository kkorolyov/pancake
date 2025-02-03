package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.graphics.PixelBuffer
import java.nio.ByteBuffer

/**
 * A texture map.
 */
class Texture(
	wrapS: Wrap = Wrap.CLAMP_TO_EDGE,
	wrapT: Wrap = Wrap.CLAMP_TO_EDGE,
	filterMin: Filter = Filter.LINEAR_MIPMAP_LINEAR,
	filterMag: Filter = Filter.LINEAR,
	pixels: () -> PixelBuffer = { PixelBuffer(0, 0, 0, 0, ByteBuffer.wrap(byteArrayOf())) {} }
) : RenderResource() {
	var pixels: () -> PixelBuffer = pixels
		set(value) {
			field = value
			invalidate()
		}

	var wrapS: Wrap = wrapS
		set(value) {
			field = value
			invalidate()
		}
	var wrapT: Wrap = wrapT
		set(value) {
			field = value
			invalidate()
		}
	var filterMin: Filter = filterMin
		set(value) {
			field = value
			invalidate()
		}
	var filterMag: Filter = filterMag
		set(value) {
			field = value
			invalidate()
		}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Texture) return false

		if (wrapS != other.wrapS) return false
		if (wrapT != other.wrapT) return false
		if (filterMin != other.filterMin) return false
		if (filterMag != other.filterMag) return false

		return true
	}

	override fun hashCode(): Int {
		var result = wrapS.hashCode()
		result = 31 * result + wrapT.hashCode()
		result = 31 * result + filterMin.hashCode()
		result = 31 * result + filterMag.hashCode()
		return result
	}

	/**
	 * Texture wrapping mode.
	 */
	enum class Wrap {
		REPEAT,
		MIRRORED_REPEAT,
		CLAMP_TO_EDGE,
		CLAMP_TO_BORDER
	}
	/**
	 * Texture filtering mode.
	 */
	enum class Filter {
		NEAREST,
		LINEAR,
		NEAREST_MIPMAP_NEAREST,
		LINEAR_MIPMAP_NEAREST,
		NEAREST_MIPMAP_LINEAR,
		LINEAR_MIPMAP_LINEAR,
	}
}
