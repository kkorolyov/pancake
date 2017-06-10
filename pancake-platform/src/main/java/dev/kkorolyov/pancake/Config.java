package dev.kkorolyov.pancake;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import dev.kkorolyov.simplelogs.Level;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simplelogs.format.Formatters;
import dev.kkorolyov.simpleprops.Properties;

/**
 * Provides access to configuration.
 */
public final class Config {
	public static Properties config;

	static {
		reloadConfig();
		reloadLogging();
	}

	private Config() {}

	/**
	 * Reloads configuration file.
	 */
	public static void reloadConfig() {
		config = loadProperties("pancake.defaults.ini");
		config.put(loadProperties("pancake.ini"), true);
	}
	/**
	 * Reloads logger configuration.
	 */
	public static void reloadLogging() {
		URL loggingUrl = ClassLoader.getSystemResource("logging.ini");
		if (loggingUrl != null) {
			try {
				Logger.applyProps(Paths.get(loggingUrl.toURI()));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param c class requesting logger
	 * @return logger without any appenders for {@code c}
	 */
	public static Logger getLogger(Class<?> c) {
		return Logger.getLogger(c.getName(), Level.DEBUG, Formatters.simple());
	}

	private static Properties loadProperties(String file) {
		URL loggingURL = ClassLoader.getSystemResource(file);
		if (loggingURL == null) return new Properties();

		try {
			return new Properties(Paths.get(loggingURL.toURI()));
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			return new Properties();
		}
	}
}
