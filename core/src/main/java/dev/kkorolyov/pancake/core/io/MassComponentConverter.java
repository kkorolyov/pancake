package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.movement.Mass;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;

public final class MassComponentConverter implements ComponentConverter<Mass> {
	@Override
	public Mass read(Object data) {
		return new Mass((Double) data);
	}
	@Override
	public Object write(Mass mass) {
		return mass.getValue();
	}

	@Override
	public Class<Mass> getType() {
		return Mass.class;
	}
}
