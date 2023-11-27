package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Orientation;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;
import dev.kkorolyov.pancake.platform.math.Vector3;

public final class OrientationComponentConverter implements ComponentConverter<Orientation> {
	@Override
	public Orientation read(Object data) {
		return new Orientation(ObjectConverters.get(Object.class, Iterable.class, Vector3.class).convertOut((Iterable<Number>) data));
	}
	@Override
	public Object write(Orientation orientation) {
		return ObjectConverters.get(Object.class, Iterable.class, Vector3.class).convertIn(orientation.getValue());
	}

	@Override
	public Class<Orientation> getType() {
		return Orientation.class;
	}
}
