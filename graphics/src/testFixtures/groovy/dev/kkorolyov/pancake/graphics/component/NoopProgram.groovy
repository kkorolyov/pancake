package dev.kkorolyov.pancake.graphics.component

import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3

class NoopProgram implements Program {
	private def id = 0

	@Override
	void set(int location, float value) {}
	@Override
	void set(int location, Vector2 value) {}
	@Override
	void set(int location, Vector3 value) {}
	@Override
	void set(int location, Matrix4 value) {}
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
		if (!(o instanceof NoopProgram)) return false

		NoopProgram that = (NoopProgram) o

		if (id != that.id) return false

		return true
	}
	int hashCode() {
		return (id != null ? id.hashCode() : 0)
	}
}
