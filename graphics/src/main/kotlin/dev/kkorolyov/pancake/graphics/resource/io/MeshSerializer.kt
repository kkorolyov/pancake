package dev.kkorolyov.pancake.graphics.resource.io

import dev.kkorolyov.pancake.graphics.resource.Buffer
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

/**
 * Serializes [Mesh]es.
 */
class MeshSerializer : Serializer<Mesh> {
	private val vertexBufferType = Buffer.vertex()::class.java
	private val intBufferType = Buffer.int()::class.java

	override fun write(value: Mesh, context: WriteContext) {
		context.putObject(value.vertices)
		context.putObject(value.indices)

		val textures = value.textures.toList()
		context.putInt(textures.size)
		textures.forEach(context::putObject)

		context.putInt(value.mode.ordinal)
	}

	override fun read(context: ReadContext): Mesh = Mesh(
		context.getObject(vertexBufferType),
		context.getObject(intBufferType),
		(1..context.int).map { context.getObject(Texture::class.java) },
		Mesh.Mode.entries[context.int]
	)

	override fun getType(): Class<Mesh> = Mesh::class.java
}
