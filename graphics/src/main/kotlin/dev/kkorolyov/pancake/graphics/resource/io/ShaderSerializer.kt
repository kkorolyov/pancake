package dev.kkorolyov.pancake.graphics.resource.io

import dev.kkorolyov.pancake.graphics.resource.Shader
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

/**
 * Serializes [Shader]s.
 */
class ShaderSerializer : Serializer<Shader> {
	override fun write(value: Shader, context: WriteContext) {
		context.putInt(value.type.ordinal)
		context.putString(value.source::class.java.name)
		context.putObject(value.source)
	}

	override fun read(context: ReadContext): Shader = Shader(
		Shader.Type.entries[context.int],
		context.getObject(Class.forName(context.string)) as Shader.Source
	)

	override fun getType(): Class<Shader> = Shader::class.java
}

/**
 * Serializes [Shader.Source.Raw] shader sources.
 */
class RawSourceSerializer : Serializer<Shader.Source.Raw> {
	override fun write(value: Shader.Source.Raw, context: WriteContext) {
		context.putString(value.value)
	}

	override fun read(context: ReadContext): Shader.Source.Raw = Shader.Source.Raw(context.string)

	override fun getType(): Class<Shader.Source.Raw> = Shader.Source.Raw::class.java
}

/**
 * Serializes [Shader.Source.Path] shader sources.
 */
class PathSourceSerializer : Serializer<Shader.Source.Path> {
	override fun write(value: Shader.Source.Path, context: WriteContext) {
		context.putString(value.path)
	}

	override fun read(context: ReadContext): Shader.Source.Path = Shader.Source.Path(context.string)

	override fun getType(): Class<Shader.Source.Path> = Shader.Source.Path::class.java
}
