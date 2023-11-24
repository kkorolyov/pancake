package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;

public final class VelocityComponentConverter implements ComponentConverter<Velocity> {
	@Override
	public Velocity read(Object data) {
		return new Velocity(ObjectConverters.vector3().convertOut((Iterable<Number>) data));
	}
	@Override
	public Object write(Velocity velocity) {
		return ObjectConverters.vector3().convertIn(velocity.getValue());
	}

	@Override
	public Class<Velocity> getType() {
		return Velocity.class;
	}
}
