package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;

import java.util.Arrays;

public final class PositionComponentConverter implements ComponentConverter<Position> {
	@Override
	public Position read(Object data) {
		return new Position(ObjectConverters.vector3().convert((Iterable<?>) data));
	}
	@Override
	public Object write(Position position) {
		return Arrays.asList(position.getValue().getX(), position.getValue().getY(), position.getValue().getZ());
	}

	@Override
	public Class<Position> getType() {
		return Position.class;
	}
}
