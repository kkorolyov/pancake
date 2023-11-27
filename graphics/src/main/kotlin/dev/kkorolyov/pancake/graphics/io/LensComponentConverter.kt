package dev.kkorolyov.pancake.graphics.io

import dev.kkorolyov.pancake.graphics.component.Lens
import dev.kkorolyov.pancake.platform.entity.ComponentConverter
import dev.kkorolyov.pancake.platform.io.ObjectConverters
import dev.kkorolyov.pancake.platform.math.Vector2
import java.util.BitSet

class LensComponentConverter : ComponentConverter<Lens> {
	override fun read(data: Any): Lens {
		val map = data as Map<String, Any>
		val vectorConverter = ObjectConverters.get(Any::class.java, Iterable::class.java, Vector2::class.java)

		val result = Lens(vectorConverter.convertOut(map["scale"] as Iterable<Number>), vectorConverter.convertOut(map["size"] as Iterable<Number>))
		map["offset"]?.let { result.offset.set(vectorConverter.convertOut(it as Iterable<Number>)) }
		map["mask"]?.let {
			result.mask.clear()
			result.mask.or(BitSet.valueOf(it as LongArray))
		}
		map["active"]?.let { result.active = it as Boolean }

		return result
	}

	override fun write(t: Lens): Map<String, Any> {
		val vectorConverter = ObjectConverters.get(Any::class.java, Iterable::class.java, Vector2::class.java)

		return mapOf(
			"scale" to vectorConverter.convertIn(t.scale),
			"size" to vectorConverter.convertIn(t.size),
			"offset" to vectorConverter.convertIn(t.offset),
			"mask" to t.mask.toLongArray(),
			"active" to t.active
		)
	}

	override fun getType(): Class<Lens> = Lens::class.java
}
