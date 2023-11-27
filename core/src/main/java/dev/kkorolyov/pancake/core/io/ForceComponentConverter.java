package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Force;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;
import dev.kkorolyov.pancake.platform.math.Vector3;

public final class ForceComponentConverter implements ComponentConverter<Force> {
	@Override
	public Force read(Object data) {
		return new Force(ObjectConverters.get(Object.class, Iterable.class, Vector3.class).convertOut((Iterable<Number>) data));
	}
	@Override
	public Object write(Force force) {
		return ObjectConverters.get(Object.class, Iterable.class, Vector3.class).convertIn(force.getValue());
	}

	@Override
	public Class<Force> getType() {
		return Force.class;
	}
}
