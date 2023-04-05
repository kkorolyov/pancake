package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Bounds;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;

public final class BoundsComponentConverter implements ComponentConverter<Bounds> {
	@Override
	public Bounds read(Object data) {
		// TODO
		return null;
	}
	@Override
	public Object write(Bounds bounds) {
		// TODO
		return null;
	}

	@Override
	public Class<Bounds> getType() {
		return Bounds.class;
	}
}
