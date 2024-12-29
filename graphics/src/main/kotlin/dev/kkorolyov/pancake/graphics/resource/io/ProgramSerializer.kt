package dev.kkorolyov.pancake.graphics.resource.io

import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.platform.io.RegisteredSerializer

/**
 * Serializes [Program]s through registry references.
 */
class ProgramSerializer : RegisteredSerializer<Program>() {
	override fun getType(): Class<Program> = Program::class.java
}
