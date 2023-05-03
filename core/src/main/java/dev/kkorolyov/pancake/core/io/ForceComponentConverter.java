package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Force;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;

import java.util.Arrays;

public final class ForceComponentConverter implements ComponentConverter<Force> {
	@Override
	public Force read(Object data) {
		return new Force(ObjectConverters.vector3().convert((Iterable<Number>) data));
	}
	@Override
	public Object write(Force force) {
		return Arrays.asList(force.getValue().getX(), force.getValue().getY(), force.getValue().getZ());
	}

	@Override
	public Class<Force> getType() {
		return Force.class;
	}
}
