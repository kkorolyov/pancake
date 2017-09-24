package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.simplelogs.Level;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simplelogs.format.Formatters;
import dev.kkorolyov.simpleprops.Properties;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Provides access to configuration.
 */
public final class Config {
	private static final String CONFIG_DEFAULT_PROPS = "pancake.defaults.ini";
	private static final String CONFIG_PROPS = "pancake.ini";
	private static final String LOG_PROPS = "logging.ini";
	private static final Logger log = getLogger(Config.class);

	public static final Properties config = new Properties();

	static {
		reloadConfig();
		reloadLogging();
	}

	private Config() {}

	/**
	 * Reloads configuration file.
	 */
	public static void reloadConfig() {
		try {
			Optional<Path> configDefaultProps = getPath(CONFIG_DEFAULT_PROPS);
			Optional<Path> configProps = getPath(CONFIG_PROPS);

			if (configDefaultProps.isPresent()) config.load(configDefaultProps.get());
			if (configProps.isPresent()) config.load(configProps.get());
		} catch (IOException e) {
			log.exception(Level.FATAL, e);
		}
		log.info("Reloaded config: {}", config);
	}
	/**
	 * Reloads logger configuration.
	 */
	public static void reloadLogging() {
		getPath(LOG_PROPS).ifPresent(Logger::applyProps);

		log.info("Reloaded loggers");
	}

	/**
	 * @param c class requesting logger
	 * @return logger without any appenders for {@code c}
	 */
	public static Logger getLogger(Class<?> c) {
		return Logger.getLogger(c.getName(), Level.DEBUG, Formatters.simple());
	}

	private static Optional<Path> getPath(String file) {
		try {
			return Optional.of(Paths.get(ClassLoader.getSystemResource(file).toURI()));
		} catch (FileSystemNotFoundException | URISyntaxException | NullPointerException e) {
			log.exception(Level.FATAL, e);
			return Optional.empty();
		}
	}
}
