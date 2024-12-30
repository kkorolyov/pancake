package dev.kkorolyov.pancake.graphics.component.io

import dev.kkorolyov.pancake.graphics.component.NoopProgram
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

class NoopProgramSerializer implements Serializer<Program> {
	@Override
	void write(Program value, WriteContext context) {}
	@Override
	Program read(ReadContext context) {
		return new NoopProgram()
	}

	@Override
	Class<Program> getType() {
		return Program.class
	}
}
