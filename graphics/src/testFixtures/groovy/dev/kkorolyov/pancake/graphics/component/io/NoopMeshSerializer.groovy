package dev.kkorolyov.pancake.graphics.component.io

import dev.kkorolyov.pancake.graphics.component.NoopMesh
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

class NoopMeshSerializer implements Serializer<Mesh> {
	@Override
	void write(Mesh value, WriteContext context) {}
	@Override
	Mesh read(ReadContext context) {
		return new NoopMesh()
	}

	@Override
	Class<Mesh> getType() {
		return Mesh.class
	}
}
