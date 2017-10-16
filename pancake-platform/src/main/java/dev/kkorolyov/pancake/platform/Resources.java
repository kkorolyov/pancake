package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.simplefiles.Files;
import dev.kkorolyov.simplefiles.stream.StreamStrategies;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Provides access to resources.
 */
public final class Resources {
	private Resources() {}

	/**
	 * Retrieves an input stream to a resource.
	 * @param path path to resource
	 * @return input stream to resource
	 */
	public static InputStream in(String path) {
		return Files.in(path, StreamStrategies.IN_PATH, StreamStrategies.IN_CLASSPATH);
	}
	/**
	 * Retrieves an output stream to a resource.
	 * @param path path to resource
	 * @return output stream to resource
	 */
	public static OutputStream out(String path) {
		return Files.out(path, StreamStrategies.OUT_PATH);
	}

	/**
	 * Reads a resource into a string.
	 * @param path path to resource
	 * @return resource text as string
	 */
	public static String string(String path) {
		return new String(Files.bytes(in(path)), StandardCharsets.UTF_8);
	}
	/**
	 * Writes a string to a resource.
	 * @param path path to resource
	 * @param s string to write
	 */
	public static void string(String path, String s) {
		Files.bytes(out(path), s.getBytes(StandardCharsets.UTF_8));
	}
}
