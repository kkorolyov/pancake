package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Orientation;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;

public final class OrientationComponentConverter implements ComponentConverter<Orientation> {
	@Override
	public Orientation read(Object data) {
		return new Orientation(ObjectConverters.vector3().convertOut((Iterable<Number>) data));
	}
	@Override
	public Object write(Orientation orientation) {
		return ObjectConverters.vector3().convertIn(orientation.getValue());
	}

	@Override
	public Class<Orientation> getType() {
		return Orientation.class;
	}
}
