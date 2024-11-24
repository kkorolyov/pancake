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
		context.putString(value.program::class.qualifiedName)
		context.putObject(value.program)

		context.putInt(value.meshes.size)
		value.meshes.forEach {
			context.putString(it::class.qualifiedName)
			context.putObject(it)
		}

		context.putObject(value.offset)
	}

	override fun read(context: ReadContext): Model = Model(
		context.getObject(Class.forName(context.string) as Class<Program>),
		*(1..context.int).map {
			context.getObject(Class.forName(context.string) as Class<Mesh>)
		}.toTypedArray(),
		offset = context.getObject(Matrix4::class.java)
	)

	override fun getType(): Class<Model> = Model::class.java
}
