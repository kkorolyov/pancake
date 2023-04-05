package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.movement.Damping;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;

import java.util.Arrays;

public final class DampingComponentConverter implements ComponentConverter<Damping> {
	@Override
	public Damping read(Object data) {
		return new Damping(ObjectConverters.vector3().convert((Iterable<Number>) data));
	}
	@Override
	public Object write(Damping damping) {
		return Arrays.asList(damping.getValue().getX(), damping.getValue().getY(), damping.getValue().getZ());
	}

	@Override
	public Class<Damping> getType() {
		return Damping.class;
	}
}
