package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.simplelogs.Level;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simplelogs.format.Formatters;
import dev.kkorolyov.simpleprops.Properties;

import static dev.kkorolyov.pancake.platform.Resources.in;

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
		in(CONFIG_DEFAULT_PROPS, config::load);
		in(CONFIG_PROPS, config::load);

		log.info("Reloaded config: {}", config);
	}
	/**
	 * Reloads logger configuration.
	 */
	public static void reloadLogging() {
		in(LOG_PROPS, Logger::applyProps);

		log.info("Reloaded loggers");
	}

	/**
	 * @param c class requesting logger
	 * @return logger without any appenders for {@code c}
	 */
	public static Logger getLogger(Class<?> c) {
		return Logger.getLogger(c.getName(), Level.DEBUG, Formatters.simple());
	}
}
