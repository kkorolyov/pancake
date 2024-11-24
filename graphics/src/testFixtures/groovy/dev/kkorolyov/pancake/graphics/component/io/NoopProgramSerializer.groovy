package dev.kkorolyov.pancake.graphics.component.io

import dev.kkorolyov.pancake.graphics.component.NoopProgram
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

class NoopProgramSerializer implements Serializer<NoopProgram> {
	@Override
	void write(NoopProgram value, WriteContext context) {}
	@Override
	NoopProgram read(ReadContext context) {
		return new NoopProgram()
	}

	@Override
	Class<NoopProgram> getType() {
		return NoopProgram.class
	}
}
