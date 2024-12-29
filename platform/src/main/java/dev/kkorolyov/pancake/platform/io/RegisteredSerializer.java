package dev.kkorolyov.pancake.platform.io;

import dev.kkorolyov.pancake.platform.Registry;

/**
 * A {@link Serializer} that simply references values from {@link Registry} bound to its {@link #getType()}.
 */
public abstract class RegisteredSerializer<T> implements Serializer<T> {
	@Override
	public void write(T value, WriteContext context) {
		context.putString(Registry.get(getType()).lookup(value));
	}
	@Override
	public T read(ReadContext context) {
		return Registry.get(getType()).get(context.getString());
	}
}
