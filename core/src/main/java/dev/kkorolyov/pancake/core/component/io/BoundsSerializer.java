package dev.kkorolyov.pancake.core.component.io;

import dev.kkorolyov.flub.data.Graph;
import dev.kkorolyov.pancake.core.component.Bounds;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;

/**
 * Serializes {@link Bounds}.
 */
public final class BoundsSerializer implements Serializer<Bounds> {
	@Override
	public void write(Bounds value, WriteContext context) {
		// TODO
	}
	@Override
	public Bounds read(ReadContext context) {
		// TODO
		return new Bounds(new Graph<>());
	}

	@Override
	public Class<Bounds> getType() {
		return Bounds.class;
	}
}
