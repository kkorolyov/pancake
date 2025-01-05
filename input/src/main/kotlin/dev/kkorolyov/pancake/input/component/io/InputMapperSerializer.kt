package dev.kkorolyov.pancake.input.component.io

import dev.kkorolyov.pancake.input.component.InputMapper
import dev.kkorolyov.pancake.input.control.Control
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

/**
 * Serializes [InputMapper]s.
 */
class InputMapperSerializer : Serializer<InputMapper> {
	override fun write(value: InputMapper, context: WriteContext) {
		context.putInt(value.size)
		value.forEach {
			context.putString(it::class.qualifiedName)
			context.putObject(it)
		}
	}

	override fun read(context: ReadContext): InputMapper {
		val result = InputMapper()

		for (i in 0..<context.int) {
			result += context.getObject(Class.forName(context.string) as Class<Control>)
		}

		return result
	}

	override fun getType(): Class<InputMapper> = InputMapper::class.java
}
