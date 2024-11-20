package dev.kkorolyov.pancake.core.io.component;

import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Serializes {@link Velocity} instances.
 */
public final class VelocitySerializer implements Serializer<Velocity> {
	@Override
	public void write(Velocity value, WriteContext context) {
		context.putObject(value.getLinear());
		context.putObject(value.getAngular());
	}
	@Override
	public Velocity read(ReadContext context) {
		var result = new Velocity();

		result.getLinear().set(context.getObject(Vector3.class));
		result.getAngular().set(context.getObject(Vector3.class));

		return result;
	}

	@Override
	public Class<Velocity> getType() {
		return Velocity.class;
	}
}
