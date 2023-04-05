package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.tag.Correctable;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;

public final class CorrectableComponentConverter implements ComponentConverter<Correctable> {
	@Override
	public Correctable read(Object data) {
		return new Correctable((Integer) data);
	}
	@Override
	public Object write(Correctable correctable) {
		return correctable.getPriority();
	}

	@Override
	public Class<Correctable> getType() {
		return Correctable.class;
	}
}
