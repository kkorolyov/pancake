package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.simplefiles.Files;
import dev.kkorolyov.simplefiles.stream.InStrategy;
import dev.kkorolyov.simplefiles.stream.OutStrategy;
import dev.kkorolyov.simplefiles.stream.StreamStrategies;
import dev.kkorolyov.simplelogs.Logger;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

	/**
	 * Returns all service providers of a type.
	 * Providers are instantiated using the constructor matching the types of {@code parameters}.
	 * If providers do not contain a constructor matching {@code parameters}, the no-arg constructor is used instead.
	 * @param providerType provider base type
	 * @param parameters constructor parameters for new instances of {@code providerType}
	 */
	public static <T> Collection<T> providers(Class<T> providerType, Object... parameters) {
		try {
			return Collections.list(ClassLoader.getSystemResources("META-INF/services/" + providerType.getName())).stream()
					.flatMap(url -> {
						try (BufferedReader in = Files.read(url.openStream())) {
							return in.lines()
									.map(name -> instantiate(name, parameters))
									.map(providerType::cast)
									.collect(Collectors.toSet())	// Intermediate collection to allow closing the reader
									.stream();
						} catch (IOException e) {
							throw new UncheckedIOException(e);
						}
					}).collect(Collectors.toSet());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	private static Object instantiate(String name, Object... parameters) {
		Class<?> c;
		try {
			c = Class.forName(name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		Constructor<?> constructor;
		boolean noArg = false;
		try {
			constructor = c.getConstructor(
					Arrays.stream(parameters)
					.map(Object::getClass)
					.toArray(Class[]::new));
		} catch (NoSuchMethodException e) {
			try {
				constructor = c.getConstructor();
				noArg = true;
			} catch (NoSuchMethodException e1) {
				throw new RuntimeException(c + " contains no constructor matching parameters " + Arrays.toString(parameters) + " nor a no-arg constructor");
			}
		}
		try {
			return constructor.newInstance(noArg ? null : parameters);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private static <T extends Closeable> T logRetrieval(String streamType, String path, T stream) {
		logRetrieval(streamType, path, stream != null);

		return stream;
	}
	private static boolean logRetrieval(String streamType, String path, boolean success) {
		if (log != null) {	// TODO Can happen during the back-forth initialization of Resources and Config
			if (success) log.debug("Retrieved {} stream at path: {}", streamType, path);
			else log.warning("Unable to find {} stream at path: {}", streamType, path);
		}
		return success;
	}
}
