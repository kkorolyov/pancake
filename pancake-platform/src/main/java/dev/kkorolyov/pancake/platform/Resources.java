package dev.kkorolyov.pancake.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Provides safe access to resources.
 * IO exceptions are caught and logged.
 */
public final class Resources {
	private static final Logger LOG = LoggerFactory.getLogger(Resources.class);

	private Resources() {}

	/**
	 * Retrieves an input stream to a resource.
	 * Attempts to load occurrences in order: [path, classpath].
	 * @param path path to resource
	 * @return input stream to resource; or {@code null} if no such resource
	 */
	public static InputStream inStream(String path) {
		InputStream result;

		try {
			result = Files.newInputStream(Paths.get(path));
			LOG.info("Loaded from path [{}]", path);
		} catch (IOException e) {
			LOG.warn("Failed to load from path [{}] - {}", path, e);
			result = ClassLoader.getSystemResourceAsStream(path);

			if (result != null) {
				LOG.info("Loaded from classpath [{}]", path);
			} else {
				LOG.warn("Failed to load from classpath [{}]", path);
			}
		}
		if (result == null) {
			LOG.error("Failed to load resource [{}]", path);
		}
		return result;
	}

	/**
	 * Retrieves an output stream to a resource.
	 * @param path path to resource
	 * @throws UncheckedIOException if an IO error occurs
	 */
	public static OutputStream outStream(String path) {
		try {
			OutputStream result = Files.newOutputStream(Paths.get(path));
			LOG.info("Got handle [{}]", path);
			return result;
		} catch (IOException e) {
			LOG.error("Failed to get handle", e);
			throw new UncheckedIOException(e);
		}
	}
}
