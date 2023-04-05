package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.limit.VelocityLimit;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;

public final class VelocityLimitComponentConverter implements ComponentConverter<VelocityLimit> {
	@Override
	public VelocityLimit read(Object data) {
		return new VelocityLimit(((Number) data).doubleValue());
	}
	@Override
	public Object write(VelocityLimit velocityLimit) {
		return velocityLimit.getValue();
	}

	@Override
	public Class<VelocityLimit> getType() {
		return VelocityLimit.class;
	}
}
