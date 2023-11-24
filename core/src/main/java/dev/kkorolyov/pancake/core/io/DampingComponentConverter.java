package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Damping;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;

public final class DampingComponentConverter implements ComponentConverter<Damping> {
	@Override
	public Damping read(Object data) {
		return new Damping(ObjectConverters.vector3().convertOut((Iterable<Number>) data));
	}
	@Override
	public Object write(Damping damping) {
		return ObjectConverters.vector3().convertIn(damping.getValue());
	}

	@Override
	public Class<Damping> getType() {
		return Damping.class;
	}
}
