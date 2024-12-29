package dev.kkorolyov.pancake.graphics.resource.io

import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.platform.io.RegisteredSerializer

/**
 * Serializes [Mesh]es by referencing the registry.
 */
class MeshSerializer : RegisteredSerializer<Mesh>() {
	override fun getType(): Class<Mesh> = Mesh::class.java
}
