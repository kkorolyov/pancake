package dev.kkorolyov.pancake.platform.component.io

import dev.kkorolyov.pancake.platform.component.DummyComponent
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

class DummyComponentSerializer implements Serializer<DummyComponent> {
	@Override
	void write(DummyComponent value, WriteContext context) {
		context.putInt(value.value)
	}
	@Override
	DummyComponent read(ReadContext context) {
		return new DummyComponent(context.getInt())
	}

	@Override
	Class<DummyComponent> getType() {
		return DummyComponent.class
	}
}
