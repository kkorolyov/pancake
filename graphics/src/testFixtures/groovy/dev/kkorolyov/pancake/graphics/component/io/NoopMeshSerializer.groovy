package dev.kkorolyov.pancake.graphics.component.io

import dev.kkorolyov.pancake.graphics.component.NoopMesh
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

class NoopMeshSerializer implements Serializer<NoopMesh> {
	@Override
	void write(NoopMesh value, WriteContext context) {}
	@Override
	NoopMesh read(ReadContext context) {
		return new NoopMesh()
	}

	@Override
	Class<NoopMesh> getType() {
		return NoopMesh.class
	}
}
