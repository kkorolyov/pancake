package dev.kkorolyov.pancake.graphics.resource.io

import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.graphics.resource.Shader
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

/**
 * Serializes [Program]s.
 */
class ProgramSerializer : Serializer<Program> {
	override fun write(value: Program, context: WriteContext) {
		val shaders = value.shaders.toList()
		context.putInt(shaders.size)
		shaders.forEach(context::putObject)

		context.putInt(value.uniforms.size)
		value.uniforms.forEach { (k, v) ->
			context.putInt(k)
			context.putString(v::class.java.name)
			context.putObject(v)
		}
	}

	override fun read(context: ReadContext): Program =
		Program(*(1..context.int).map { context.getObject(Shader::class.java) }.toTypedArray()).apply {
			repeat(context.int) {
				uniforms.setRaw(context.int, context.getObject(Class.forName(context.string)))
			}
		}

	override fun getType(): Class<Program> = Program::class.java
}
