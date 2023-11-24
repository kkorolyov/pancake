package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;

public final class PositionComponentConverter implements ComponentConverter<Position> {
	@Override
	public Position read(Object data) {
		return new Position(ObjectConverters.vector3().convertOut((Iterable<Number>) data));
	}
	@Override
	public Object write(Position position) {
		return ObjectConverters.vector3().convertIn(position.getValue());
	}

	@Override
	public Class<Position> getType() {
		return Position.class;
	}
}
