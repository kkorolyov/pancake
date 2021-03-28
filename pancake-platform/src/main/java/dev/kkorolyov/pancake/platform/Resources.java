package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.application.Application;
import dev.kkorolyov.pancake.platform.media.audio.AudioFactory;
import dev.kkorolyov.pancake.platform.media.graphic.RenderMedium;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * Provides safe access to resources.
 * IO exceptions are caught and logged.
 */
public final class Resources {
	private static final Logger LOG = LoggerFactory.getLogger(Resources.class);

	/** Service-loaded application executor */
	public static final Application APPLICATION = ServiceLoader.load(Application.class).findFirst().orElse(null);
	/** Service-loaded audio factory */
	public static final AudioFactory AUDIO_FACTORY = ServiceLoader.load(AudioFactory.class).findFirst().orElse(null);
	/** Service-loaded render medium */
	public static final RenderMedium RENDER_MEDIUM = ServiceLoader.load(RenderMedium.class).findFirst().orElse(null);

	private Resources() {}

	/**
	 * Retrieves an input stream to a resource.
	 * Attempts to load occurrences in order: [path, classpath].
	 * @param path path to resource
	 * @return input stream to resource
	 */
	public static Optional<InputStream> inStream(String path) {
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
		return Optional.ofNullable(result);
	}

	/**
	 * Retrieves an output stream to a resource.
	 * @param path path to resource
	 * @return output stream to resource; or {@code null} if no such resource
	 */
	public static Optional<OutputStream> outStream(String path) {
		Optional<OutputStream> result = Optional.empty();
		try {
			result = Optional.of(Files.newOutputStream(Paths.get(path)));
			LOG.info("Got handle [{}]", path);
		} catch (IOException e) {
			LOG.error("Failed to get handle", e);
		}
		return result;
	}
}
