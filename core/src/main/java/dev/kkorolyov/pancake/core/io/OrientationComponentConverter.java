package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Orientation;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;

import java.util.Arrays;

public final class OrientationComponentConverter implements ComponentConverter<Orientation> {
	@Override
	public Orientation read(Object data) {
		return new Orientation(ObjectConverters.vector3().convert((Iterable<?>) data));
	}
	@Override
	public Object write(Orientation orientation) {
		return Arrays.asList(orientation.getValue().getX(), orientation.getValue().getY(), orientation.getValue().getZ());
	}

	@Override
	public Class<Orientation> getType() {
		return Orientation.class;
	}
}
