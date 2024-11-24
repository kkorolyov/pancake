package dev.kkorolyov.pancake.graphics.component.io

import dev.kkorolyov.pancake.graphics.component.Lens
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Vector2
import java.util.BitSet

/**
 * Serializes [Lens] instances.
 */
class LensSerializer : Serializer<Lens> {
	override fun write(value: Lens, context: WriteContext) {
		context.putObject(value.scale)
		context.putObject(value.size)
		context.putObject(value.offset)

		val maskLongs = value.mask.toByteArray()
		context.putInt(maskLongs.size)
		maskLongs.forEach {
			context.putByte(it)
		}

		context.putBoolean(value.active)
	}

	override fun read(context: ReadContext): Lens = Lens(
		context.getObject(Vector2::class.java),
		context.getObject(Vector2::class.java),
		context.getObject(Vector2::class.java),
		BitSet.valueOf((1..context.int).map {
			context.byte
		}.toByteArray()),
		context.boolean
	)

	override fun getType(): Class<Lens> = Lens::class.java
}
