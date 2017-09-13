package dev.kkorolyov.pancake.muffin.persistence;

/**
 * Reads and writes persisted data from and to strings.
 */
public abstract class DataPersister<T> {
	/**
	 * Reads a data instance from a string.
	 * @param s string to read
	 * @return parsed instance of {@code T}
	 */
	public abstract T read(String s);
	/**
	 * Writes a data instance to a string.
	 * @param instance instance to write
	 * @return persistable string representation of {@code instance}
	 */
	public abstract String write(T instance);

}
