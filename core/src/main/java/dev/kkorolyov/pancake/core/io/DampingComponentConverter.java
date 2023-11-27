package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Damping;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;
import dev.kkorolyov.pancake.platform.math.Vector3;

public final class DampingComponentConverter implements ComponentConverter<Damping> {
	@Override
	public Damping read(Object data) {
		return new Damping(ObjectConverters.get(Object.class, Iterable.class, Vector3.class).convertOut((Iterable<Number>) data));
	}
	@Override
	public Object write(Damping damping) {
		return ObjectConverters.get(Object.class, Iterable.class, Vector3.class).convertIn(damping.getValue());
	}

	@Override
	public Class<Damping> getType() {
		return Damping.class;
	}
}
