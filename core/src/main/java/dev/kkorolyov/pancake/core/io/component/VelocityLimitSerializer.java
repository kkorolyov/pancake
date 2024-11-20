package dev.kkorolyov.pancake.core.io.component;

import dev.kkorolyov.pancake.core.component.limit.VelocityLimit;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;

/**
 * Serializes {@link VelocityLimit}s.
 */
public final class VelocityLimitSerializer implements Serializer<VelocityLimit> {
	@Override
	public void write(VelocityLimit value, WriteContext context) {
		context.putDouble(value.getLinear());
		context.putDouble(value.getAngular());
	}
	@Override
	public VelocityLimit read(ReadContext context) {
		return new VelocityLimit(context.getDouble(), context.getDouble());
	}

	@Override
	public Class<VelocityLimit> getType() {
		return VelocityLimit.class;
	}
}
