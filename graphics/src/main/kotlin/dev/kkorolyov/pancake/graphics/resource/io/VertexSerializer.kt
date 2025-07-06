package dev.kkorolyov.pancake.graphics.resource.io

import dev.kkorolyov.pancake.graphics.resource.Vertex
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Vector2

/**
 * Serializes [Vertex]s.
 */
class VertexSerializer : Serializer<Vertex> {
	override fun write(value: Vertex, context: WriteContext) {
		context.putInt(value.size)
		value.forEach {
			context.putString(it::class.java.name)
			context.putObject(it)
		}
	}

	override fun read(context: ReadContext): Vertex =
		Vertex(*(1..context.int).map { context.getObject(Class.forName(context.string)) as Vector2 }.toTypedArray())

	override fun getType(): Class<Vertex> = Vertex::class.java
}
