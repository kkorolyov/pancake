package dev.kkorolyov.pancake.graphics.component

import dev.kkorolyov.pancake.graphics.resource.Mesh
import kotlin.Pair

class NoopMesh implements Mesh {
	private def id = 0

	@Override
	List<List<Pair<Double, Double>>> getBounds() {
		return []
	}
	@Override
	void draw(int offset, Integer count) {}
	@Override
	void activate() {}
	@Override
	void deactivate() {}
	@Override
	int getId() {
		return id
	}
	@Override
	void close() {}

	boolean equals(o) {
		if (this.is(o)) return true
		if (!(o instanceof NoopMesh)) return false

		NoopMesh noopMesh = (NoopMesh) o

		if (id != noopMesh.id) return false

		return true
	}
	int hashCode() {
		return (id != null ? id.hashCode() : 0)
	}
}
