package dev.kkorolyov.pancake.graphics.resource.io

import dev.kkorolyov.pancake.graphics.resource.Buffer
import dev.kkorolyov.pancake.graphics.resource.Vertex
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

/**
 * Serializes [Vertex] [Buffer]s.
 */
class BufferSerializerVertex : Serializer<Buffer<Vertex>> {
	private val type = Buffer.vertex()::class.java as Class<Buffer<Vertex>>

	override fun write(value: Buffer<Vertex>, context: WriteContext) {
		context.putInt(value.size)
		value.forEach(context::putObject)
	}

	override fun read(context: ReadContext): Buffer<Vertex> =
		Buffer.vertex(*(1..context.int).map { context.getObject(Vertex::class.java) }.toTypedArray())

	override fun getType(): Class<Buffer<Vertex>> = type
}

/**
 * Serializes [Int] [Buffer]s.
 */
class BufferSerializerInt : Serializer<Buffer<Int>> {
	private val type = Buffer.int()::class.java as Class<Buffer<Int>>

	override fun write(value: Buffer<Int>, context: WriteContext) {
		context.putInt(value.size)
		value.forEach(context::putInt)
	}

	override fun read(context: ReadContext): Buffer<Int> =
		Buffer.int(*(1..context.int).map { context.int }.toIntArray())

	override fun getType(): Class<Buffer<Int>> = type
}
