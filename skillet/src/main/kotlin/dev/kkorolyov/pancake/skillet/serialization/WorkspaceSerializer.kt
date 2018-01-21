package dev.kkorolyov.pancake.skillet.serialization

import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer
import dev.kkorolyov.pancake.skillet.model.Workspace

/**
 * Serializes [Workspace].
 */
object WorkspaceSerializer : StringSerializer<Workspace>("""(,\s*${GenericEntitySerializer.pattern()})*""") {
	override fun read(out: String): Workspace =
			Workspace().apply {
				Iterable { GenericEntitySerializer.matches(out).iterator() }
						.forEach { add(it) }
			}
	override fun write(`in`: Workspace): String =
			`in`.entities.joinToString(",${System.lineSeparator()}") { GenericEntitySerializer.write(it) }
}
