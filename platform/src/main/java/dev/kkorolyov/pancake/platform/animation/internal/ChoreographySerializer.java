package dev.kkorolyov.pancake.platform.animation.internal;

import dev.kkorolyov.pancake.platform.animation.Choreography;
import dev.kkorolyov.pancake.platform.animation.Timeline;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;

/**
 * Serializes {@link Choreography} instances.
 */
public final class ChoreographySerializer implements Serializer<Choreography<?>> {
	@Override
	public void write(Choreography<?> value, WriteContext context) {
		context.putInt(value.size());
		for (Choreography.Role<?> role : value) {
			context.putString(role.key());
			context.putObject(role.timeline());
		}
	}
	@Override
	public Choreography<?> read(ReadContext context) {
		var result = new Choreography<>();

		var size = context.getInt();
		for (int i = 0; i < size; i++) {
			result.put(context.getString(), context.getObject(Timeline.class));
		}

		return result;
	}

	@Override
	public Class<Choreography<?>> getType() {
		return (Class) Choreography.class;
	}
}
