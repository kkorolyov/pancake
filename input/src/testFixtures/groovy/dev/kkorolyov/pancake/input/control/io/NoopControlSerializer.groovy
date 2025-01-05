package dev.kkorolyov.pancake.input.control.io

import dev.kkorolyov.pancake.input.control.NoopControl
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

class NoopControlSerializer implements Serializer<NoopControl> {
	@Override
	void write(NoopControl value, WriteContext context) {}
	@Override
	NoopControl read(ReadContext context) {
		return NoopControl.INSTANCE
	}

	@Override
	Class<NoopControl> getType() {
		return NoopControl
	}
}
