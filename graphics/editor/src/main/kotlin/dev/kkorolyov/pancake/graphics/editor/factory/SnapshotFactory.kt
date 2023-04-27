package dev.kkorolyov.pancake.graphics.editor.factory

import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.graphics.editor.Snapshot
import dev.kkorolyov.pancake.graphics.resource.Mesh
import java.util.ServiceLoader

private val factories by ThreadLocal.withInitial { ServiceLoader.load(SnapshotFactory::class.java) }

/**
 * Returns the most suitable snapshot for [mesh].
 * Throws [UnsupportedOperationException] if no suitable provider found.
 */
fun getSnapshot(mesh: Mesh, width: Int, height: Int): Snapshot = factories.firstNotNullOfOrNull { it.get(mesh, width, height) } ?: throw UnsupportedOperationException("no provider found for mesh [$mesh]")

/**
 * Returns snapshots for capturing [Mesh] instances.
 */
interface SnapshotFactory {
	/**
	 * Returns a snapshot of [width] and [height] for [mesh], if this factory handles it.
	 */
	fun get(mesh: Mesh, width: Int, height: Int): Snapshot?
}
