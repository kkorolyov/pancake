package dev.kkorolyov.pancake.graphics.gl.resource.io

import dev.kkorolyov.pancake.graphics.gl.resource.GLProgram
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

/**
 * Serializes [GLProgram]s.
 */
class GLProgramSerializer : Serializer<GLProgram> {
	override fun write(value: GLProgram, context: WriteContext) {
		value
	}

	override fun read(context: ReadContext): GLProgram = GLProgram()

	override fun getType(): Class<GLProgram> = GLProgram::class.java
}
