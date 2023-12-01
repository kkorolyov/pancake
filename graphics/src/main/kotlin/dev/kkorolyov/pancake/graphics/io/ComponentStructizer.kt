package dev.kkorolyov.pancake.graphics.io

import dev.kkorolyov.pancake.graphics.component.Lens
import dev.kkorolyov.pancake.graphics.component.Model
import dev.kkorolyov.pancake.platform.io.Structizer
import dev.kkorolyov.pancake.platform.io.Structizers
import dev.kkorolyov.pancake.platform.math.Vector2
import java.util.BitSet
import java.util.Optional

/**
 * [Structizer] for graphics components.
 */
class ComponentStructizer : Structizer {
	override fun toStruct(o: Any): Optional<Any> = Optional.of(o)
		.map(Structizer.first(
			Structizer.select(Lens::class.java) {
				mapOf(
					"scale" to Structizers.toStruct(it.scale),
					"size" to Structizers.toStruct(it.size),
					"offset" to Structizers.toStruct(it.offset),
					"mask" to it.mask.toLongArray().asList(),
					"active" to it.active
				)
			},
			// TODO
			Structizer.select(Model::class.java) { "" }
		))

	override fun <T> fromStruct(c: Class<T>, o: Any): Optional<T> = Optional.of(o)
		.map<Map<String, Any>> { if (it is Map<*, *> && it.keys.all { it is String }) it as Map<String, Any> else null }
		.map(Structizer.first(
			Structizer.select(c, Lens::class.java) {
				val result = Lens(Structizers.fromStruct(Vector2::class.java, it["scale"] as Iterable<Number>), Structizers.fromStruct(Vector2::class.java, it["size"] as Iterable<Number>))

				it["offset"]?.let { result.offset.set(Structizers.fromStruct(Vector2::class.java, it as Iterable<Number>)) }
				it["mask"]?.let {
					result.mask.clear()
					result.mask.or(BitSet.valueOf((it as Collection<Long>).toLongArray()))
				}
				it["active"]?.let { result.active = it as Boolean }

				result
			},
			Structizer.select(c, Model::class.java) {
				TODO("Not yet implemented")
			}
		))
}
