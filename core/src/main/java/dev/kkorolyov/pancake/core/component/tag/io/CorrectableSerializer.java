package dev.kkorolyov.pancake.core.component.tag.io;

import dev.kkorolyov.pancake.core.component.tag.Correctable;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;

/**
 * Serializes {@link Correctable}s.
 */
public final class CorrectableSerializer implements Serializer<Correctable> {
	@Override
	public void write(Correctable value, WriteContext context) {
		context.putInt(value.getPriority());
	}
	@Override
	public Correctable read(ReadContext context) {
		return new Correctable(context.getInt());
	}

	@Override
	public Class<Correctable> getType() {
		return Correctable.class;
	}
}
