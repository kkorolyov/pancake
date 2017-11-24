package dev.kkorolyov.pancake.skillet.serialization

import dev.kkorolyov.pancake.platform.serialization.string.entity.ComponentStringSerializer
import dev.kkorolyov.pancake.skillet.model.GenericComponent

private val namePattern = """\w+(?=\{)""".toRegex()

object GenericComponentSerializer : ComponentStringSerializer<GenericComponent>("""\w+""") {
	override fun read(out: String): GenericComponent =
			GenericComponent(
					namePattern.matchEntire(out)?.groups?.first()?.value
							?: throw IllegalArgumentException("Does not contain a component name: $out"),
					readMap(out))
	override fun write(`in`: GenericComponent): String =
			`in`.name + writeMap(`in`.attributes)
}
