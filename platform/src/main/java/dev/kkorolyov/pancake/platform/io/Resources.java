package dev.kkorolyov.pancake.platform.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
			LOG.info("Got stream from path [{}]", path);
		} catch (IOException e) {
			LOG.warn("Failed to get stream from path [{}] - {}", path, e);

			result = ClassLoader.getSystemResourceAsStream(path);
			if (result != null) {
				LOG.info("Got stream from classpath [{}]", path);
			} else {
				LOG.warn("Failed to get stream from classpath [{}]", path);
			}
		}
		if (result == null) {
			LOG.error("Failed to get stream for resource [{}]", path);
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

	/**
	 * Returns a readable channel to a resource at {@code path}.
	 * Attempts locations in order: [path, classpath].
	 */
	public static ReadableByteChannel read(String path) {
		ReadableByteChannel result = null;

		try {
			result = Files.newByteChannel(Paths.get(path), StandardOpenOption.READ);
			LOG.info("Got read handle to path [{}]", path);
		} catch (IOException e) {
			LOG.warn("Failed to get read handle to path [{}] - {}", path, e);

			var stream = ClassLoader.getSystemResourceAsStream(path);
			if (stream != null) {
				result = Channels.newChannel(stream);
				LOG.info("Got read handle to classpath [{}]", path);
			} else {
				LOG.warn("Failed to get read handle to classpath [{}]", path);

				throw new UncheckedIOException(new NoSuchFileException(path));
			}
		}
		return result;
	}

	/**
	 * Returns a writable channel to a resource at {@code path}.
	 */
	public static WritableByteChannel write(String path) {
		try {
			WritableByteChannel result = Files.newByteChannel(Paths.get(path), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
			LOG.info("Got write handle to path [{}]", path);
			return result;
		} catch (IOException e) {
			LOG.error("Failed to get write handle to path [{}] - {}", path, e);
			throw new UncheckedIOException(e);
		}
	}
}
