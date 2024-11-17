package dev.kkorolyov.pancake.platform.io;

/**
 * Writes and reads objects to and from serialized contexts.
 * The order of element operations must be identical between write and read operations.
 */
public interface Serializer<T> {
	/**
	 * Writes {@code value} to {@code context}.
	 * Must write elements in the same order as read by {@link #read(ReadContext)}.
	 */
	void write(T value, WriteContext context);
	/**
	 * Reads a {@code T} from {@code context}.
	 * Must read elements in the same order as written by {@link #write(Object, WriteContext)}.
	 */
	T read(ReadContext context);

	/**
	 * Returns the supertype {@code T} that this serializer acts on.
	 */
	Class<T> getType();
}
