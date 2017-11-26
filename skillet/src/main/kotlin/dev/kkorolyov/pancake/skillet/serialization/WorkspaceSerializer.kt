package dev.kkorolyov.pancake.skillet.serialization

import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer
import dev.kkorolyov.pancake.skillet.model.Workspace

private val splitPattern = """,\s*(?=\w+\s*\[)""".toRegex()

/**
 * Serializes [Workspace].
 */
object WorkspaceSerializer : StringSerializer<Workspace>("""(,\s*${GenericEntitySerializer.pattern()})*""") {
	override fun read(out: String): Workspace =
			Workspace().apply {
				out.split(splitPattern)	// Split beforehand because matches() is greedy
						.flatMap { Iterable { GenericEntitySerializer.matches(it).iterator() } }
						.forEach { add(it) }
			}
	override fun write(`in`: Workspace): String =
			`in`.entities.joinToString(",${System.lineSeparator()}") { GenericEntitySerializer.write(it) }
}
