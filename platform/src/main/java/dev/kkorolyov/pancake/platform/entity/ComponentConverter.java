package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.pancake.platform.io.BasicParsers;

/**
 * Converts between {@code T} and a "basic" serializable object representation.
 * See {@link BasicParsers} for details on "basic" objects.
 * Designed for use a service provider.
 */
public interface ComponentConverter<T extends Component> {
	/**
	 * Returns an instance of {@code T} as represented by {@code data}.
	 * The accepted structure must match the structure returned by {@link #write(Component)}.
	 */
	T read(Object data);

	/**
	 * Returns a serializable representation of {@code t}.
	 * The returned structure must match the structure accepted by {@link #read(Object)}.
	 */
	Object write(T t);

	/**
	 * Returns the type of component this converts.
	 */
	Class<T> getType();
}
