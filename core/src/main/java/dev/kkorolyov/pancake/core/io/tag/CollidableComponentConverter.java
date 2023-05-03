package dev.kkorolyov.pancake.core.io.tag;

import dev.kkorolyov.pancake.core.component.tag.Collidable;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;

public final class CollidableComponentConverter implements ComponentConverter<Collidable> {
	@Override
	public Collidable read(Object data) {
		return new Collidable((Integer) data);
	}
	@Override
	public Object write(Collidable collidable) {
		return collidable.getPriority();
	}

	@Override
	public Class<Collidable> getType() {
		return Collidable.class;
	}
}
