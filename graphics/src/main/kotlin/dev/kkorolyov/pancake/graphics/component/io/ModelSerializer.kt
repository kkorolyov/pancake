package dev.kkorolyov.pancake.graphics.component.io

import dev.kkorolyov.pancake.graphics.component.Model
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Matrix4

/**
 * Serializes [Model]s.
 */
class ModelSerializer : Serializer<Model> {
	override fun write(value: Model, context: WriteContext) {
		context.putObject(value.program)

		context.putInt(value.meshes.size)
		value.meshes.forEach {
			context.putObject(it)
		}

		context.putObject(value.offset)
	}

	override fun read(context: ReadContext): Model = Model(
		context.getObject(Program::class.java),
		*(1..context.int).map {
			context.getObject(Mesh::class.java)
		}.toTypedArray(),
		offset = context.getObject(Matrix4::class.java)
	)

	override fun getType(): Class<Model> = Model::class.java
}
