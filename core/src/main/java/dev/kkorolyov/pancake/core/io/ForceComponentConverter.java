package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Force;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;

public final class ForceComponentConverter implements ComponentConverter<Force> {
	@Override
	public Force read(Object data) {
		return new Force(ObjectConverters.vector3().convertOut((Iterable<Number>) data));
	}
	@Override
	public Object write(Force force) {
		return ObjectConverters.vector3().convertIn(force.getValue());
	}

	@Override
	public Class<Force> getType() {
		return Force.class;
	}
}
