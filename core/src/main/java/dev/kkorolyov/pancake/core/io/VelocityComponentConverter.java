package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;

import java.util.Arrays;

public final class VelocityComponentConverter implements ComponentConverter<Velocity> {
	@Override
	public Velocity read(Object data) {
		return new Velocity(ObjectConverters.vector3().convert((Iterable<Number>) data));
	}
	@Override
	public Object write(Velocity velocity) {
		return Arrays.asList(velocity.getValue().getX(), velocity.getValue().getY(), velocity.getValue().getZ());
	}

	@Override
	public Class<Velocity> getType() {
		return Velocity.class;
	}
}
