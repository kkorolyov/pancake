package dev.kkorolyov.pancake.graphics.editor.factory

import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.graphics.editor.Snapshot
import dev.kkorolyov.pancake.graphics.resource.Mesh
import java.util.ServiceLoader

private val factories by ThreadLocal.withInitial { ServiceLoader.load(SnapshotFactory::class.java) }

/**
 * Returns the most suitable snapshot for [c]-type meshes.
 * Throws [UnsupportedOperationException] if no suitable provider found.
 */
fun getSnapshot(c: Class<out Mesh>, width: Int, height: Int): Snapshot = factories.firstNotNullOfOrNull { it.get(c, width, height) } ?: throw UnsupportedOperationException("no provider found for mesh [$c]")

/**
 * Returns snapshots for capturing [Mesh] instances.
 */
interface SnapshotFactory {
	/**
	 * Returns a snapshot of [width] and [height] for [c]-type meshes, if this factory handles it.
	 */
	fun get(c: Class<out Mesh>, width: Int, height: Int): Snapshot?
}
