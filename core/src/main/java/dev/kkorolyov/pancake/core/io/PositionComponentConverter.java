package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;
import dev.kkorolyov.pancake.platform.math.Vector3;

public final class PositionComponentConverter implements ComponentConverter<Position> {
	@Override
	public Position read(Object data) {
		return new Position(ObjectConverters.get(Object.class, Iterable.class, Vector3.class).convertOut((Iterable<Number>) data));
	}
	@Override
	public Object write(Position position) {
		return ObjectConverters.get(Object.class, Iterable.class, Vector3.class).convertIn(position.getValue());
	}

	@Override
	public Class<Position> getType() {
		return Position.class;
	}
}
