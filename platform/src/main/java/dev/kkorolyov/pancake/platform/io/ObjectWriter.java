package dev.kkorolyov.pancake.platform.io;

import java.io.OutputStream;

/**
 * Writes {@link T}s to an output stream.
 */
@FunctionalInterface
public interface ObjectWriter<T> {
	/**
	 * Writes {@code t} to {@code out}.
	 */
	void write(OutputStream out, T t);
}
