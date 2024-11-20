package dev.kkorolyov.pancake.core.io.component;

import dev.kkorolyov.pancake.core.component.Force;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Serializes {@link Force}es.
 */
public final class ForceSerializer implements Serializer<Force> {
	@Override
	public void write(Force value, WriteContext context) {
		context.putObject(value.getValue());
		context.putObject(value.getOffset());
	}
	@Override
	public Force read(ReadContext context) {
		var result = new Force();

		result.getValue().set(context.getObject(Vector3.class));
		result.getOffset().set(context.getObject(Vector3.class));

		return result;
	}

	@Override
	public Class<Force> getType() {
		return Force.class;
	}
}
