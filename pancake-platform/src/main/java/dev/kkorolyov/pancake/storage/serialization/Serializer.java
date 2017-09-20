package dev.kkorolyov.pancake.storage.serialization;

/**
 * Transforms data between instance and serialized representation.
 */
public interface Serializer<T> {
	/**
	 * Reads a data instance from a string.
	 * @param s string to read
	 * @return parsed instance of {@code T}
	 */
	T read(String s);
	/**
	 * Writes a data instance to a string.
	 * @param instance instance to write
	 * @return persistable string representation of {@code instance}
	 */
	String write(T instance);
}
