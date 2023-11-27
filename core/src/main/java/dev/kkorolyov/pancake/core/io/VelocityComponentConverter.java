package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;
import dev.kkorolyov.pancake.platform.math.Vector3;

public final class VelocityComponentConverter implements ComponentConverter<Velocity> {
	@Override
	public Velocity read(Object data) {
		return new Velocity(ObjectConverters.get(Object.class, Iterable.class, Vector3.class).convertOut((Iterable<Number>) data));
	}
	@Override
	public Object write(Velocity velocity) {
		return ObjectConverters.get(Object.class, Iterable.class, Vector3.class).convertIn(velocity.getValue());
	}

	@Override
	public Class<Velocity> getType() {
		return Velocity.class;
	}
}
