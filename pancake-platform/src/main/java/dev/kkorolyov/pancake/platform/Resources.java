package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.simplefiles.Files;
import dev.kkorolyov.simplefiles.stream.InStrategy;
import dev.kkorolyov.simplefiles.stream.OutStrategy;
import dev.kkorolyov.simplefiles.stream.StreamStrategies;
import dev.kkorolyov.simplelogs.Logger;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Provides access to resources.
 */
public final class Resources {
	private static final InStrategy[] IN_STRATEGIES = {
			StreamStrategies.IN_PATH,
			StreamStrategies.IN_CLASSPATH
	};
	private static final OutStrategy[] OUT_STRATEGIES = {
			StreamStrategies.OUT_PATH
	};
	private static final Logger log = Config.getLogger(Resources.class);

	private Resources() {}

	/**
	 * Retrieves an input stream to a resource.
	 * @param path path to resource
	 * @return input stream to resource
	 */
	public static InputStream in(String path) {
		return logRetrieval("input", path, Files.in(path, IN_STRATEGIES));
	}
	/**
	 * Consumes an input stream if it exists.
	 * @param path path to resource
	 * @param streamConsumer input stream consumer
	 * @return {@code true} if stream exists and was consumed
	 */
	public static boolean in(String path, Consumer<InputStream> streamConsumer) {
		return logRetrieval("input", path, Files.in(streamConsumer, path, IN_STRATEGIES));
	}

	/**
	 * Retrieves an output stream to a resource.
	 * @param path path to resource
	 * @return output stream to resource
	 */
	public static OutputStream out(String path) {
		return logRetrieval("output", path, Files.out(path, OUT_STRATEGIES));
	}
	/**
	 * Consumes an output stream if it exists.
	 * @param path path to resource
	 * @param streamConsumer output stream consumer
	 * @return {@code true} if stream exists and was consumed
	 */
	public static boolean out(String path, Consumer<OutputStream> streamConsumer) {
		return logRetrieval("output", path, Files.out(streamConsumer, path, OUT_STRATEGIES));
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

	private static <T extends Closeable> T logRetrieval(String streamType, String path, T stream) {
		logRetrieval(streamType, path, stream != null);

		return stream;
	}
	private static boolean logRetrieval(String streamType, String path, boolean success) {
		if (success) log.debug("Retrieved {} stream at path: {}", streamType, path);
		else log.warning("Unable to find {} stream at path: {}", streamType, path);

		return success;
	}
}
