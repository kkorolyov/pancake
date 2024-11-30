package dev.kkorolyov.pancake.core.component.tag.io;

import dev.kkorolyov.pancake.core.component.tag.Collidable;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;

/**
 * Serializes {@link Collidable}s.
 */
public final class CollidableSerializer implements Serializer<Collidable> {
	@Override
	public void write(Collidable value, WriteContext context) {
		context.putInt(value.getPriority());
	}
	@Override
	public Collidable read(ReadContext context) {
		return new Collidable(context.getInt());
	}

	@Override
	public Class<Collidable> getType() {
		return Collidable.class;
	}
}
