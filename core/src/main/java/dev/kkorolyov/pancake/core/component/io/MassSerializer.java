package dev.kkorolyov.pancake.core.component.io;

import dev.kkorolyov.pancake.core.component.Mass;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;

/**
 * Serializes {@link Mass}es.
 */
public final class MassSerializer implements Serializer<Mass> {
	@Override
	public void write(Mass value, WriteContext context) {
		context.putDouble(value.getValue());
	}
	@Override
	public Mass read(ReadContext context) {
		return new Mass(context.getDouble());
	}

	@Override
	public Class<Mass> getType() {
		return Mass.class;
	}
}
