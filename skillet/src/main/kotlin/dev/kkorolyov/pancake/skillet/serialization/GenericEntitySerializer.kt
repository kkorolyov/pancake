package dev.kkorolyov.pancake.skillet.serialization

import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer
import dev.kkorolyov.pancake.platform.serialization.string.entity.EntityStringSerializer
import dev.kkorolyov.pancake.skillet.model.GenericEntity

private val pattern = EntityStringSerializer(null).pattern()
private val splitPattern = """,\s*(?=\w+\s*\{)""".toRegex()
private val namePattern = """\w+(?=\s*\[)""".toRegex()

/**
 * Serializes [GenericEntity].
 */
object GenericEntitySerializer : StringSerializer<GenericEntity>(pattern) {
	override fun read(out: String): GenericEntity =
			GenericEntity(
					namePattern.find(out)?.value
							?: throw IllegalArgumentException("Does not contain an entity name: $out"),
					out.split(splitPattern) // Split beforehand because matches() is greedy
							.flatMap { Iterable { GenericComponentSerializer.matches(it).iterator() } })
	override fun write(`in`: GenericEntity): String =
			`in`.name + `in`.components.joinToString(",${System.lineSeparator()}\t", "[${System.lineSeparator()}\t", "]") {
				GenericComponentSerializer.write(it)
			}
}
