package dev.kkorolyov.pancake.core.io.component;

import dev.kkorolyov.pancake.core.component.Damping;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Serializes {@link Damping}s.
 */
public final class DampingSerializer implements Serializer<Damping> {
	@Override
	public void write(Damping value, WriteContext context) {
		context.putObject(value.getLinear());
		context.putObject(value.getAngular());
	}
	@Override
	public Damping read(ReadContext context) {
		return new Damping(context.getObject(Vector3.class), context.getObject(Vector3.class));
	}

	@Override
	public Class<Damping> getType() {
		return Damping.class;
	}
}
