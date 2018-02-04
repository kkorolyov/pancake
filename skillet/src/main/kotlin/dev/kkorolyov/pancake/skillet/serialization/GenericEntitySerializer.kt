package dev.kkorolyov.pancake.skillet.serialization

import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer
import dev.kkorolyov.pancake.platform.serialization.string.entity.EntityStringSerializer
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import java.util.stream.Stream

private val namePattern = """\w+""".toRegex()
private val baseBattern = EntityStringSerializer(null).pattern()
private val nameMatchPattern = """$namePattern(?=$baseBattern)""".toRegex()

/**
 * Serializes [GenericEntity].
 */
object GenericEntitySerializer : StringSerializer<GenericEntity>("$namePattern$baseBattern") {
	override fun read(out: String): GenericEntity =
			GenericEntity(
					nameMatchPattern.find(out)?.value ?: throw IllegalArgumentException("Does not contain an entity name: $out"),
					Iterable { GenericComponentSerializer.matches(out).iterator() }
			)
	override fun write(`in`: GenericEntity): String =
			`in`.name + `in`.components.joinToString(",${System.lineSeparator()}\t", "[${System.lineSeparator()}\t", "]") {
				GenericComponentSerializer.write(it)
			}

	override fun matches(out: String): Stream<GenericEntity> =
			out.split(""",\s*(?=${pattern()})""".toRegex()).stream()
					.flatMap { super.matches(it) }
}
