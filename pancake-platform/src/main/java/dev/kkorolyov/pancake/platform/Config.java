package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.simplelogs.Level;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simplelogs.format.Formatters;
import dev.kkorolyov.simpleprops.Properties;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Stream;

import static dev.kkorolyov.pancake.platform.Resources.in;

/**
 * Provides access to configuration.
 */
public final class Config {
	private static final String EXTENSION = ".ini";
	private static final String DEFAULT_EXTENSION = ".default" + EXTENSION;

	private static final String CONFIG_PLATFORM = "pancake";
	private static final String CONFIG_LOG = "logging";

	private static final Logger LOG = getLogger(Config.class);

	private static final Map<String, Properties> configs = new HashMap<>();

	static {
		reloadLogging();
		reloadConfig();
	}

	private Config() {}

	/**
	 * Reloads the platform configuration and all system configurations.
	 */
	public static void reloadConfig() {
		reloadConfig(CONFIG_PLATFORM);
		streamSystemConfigNames().forEach(Config::reloadConfig);
	}
	private static void reloadConfig(String name) {
		Properties config = configs.computeIfAbsent(name, k -> new Properties());
		config.clear();

		if (in(getDefaultsFileName(name), config::load)) {
			LOG.info("Reloaded default config [{}]", name);
		}
		if (in(getFileName(name), config::load)) {
			LOG.info("Reloaded config [{}]", name);
		}
	}

	private static Stream<String> streamSystemConfigNames() {
		return ServiceLoader.load(GameSystem.class).stream()
				.map(ServiceLoader.Provider::type)
				.map(Class::getName);
	}

	/**
	 * Reloads logger configuration.
	 */
	public static void reloadLogging() {
		in(getFileName(CONFIG_LOG), Logger::applyProps);

		LOG.info("Reloaded loggers");
	}

	/** @return platform configuration */
	public static Properties config() {
		return configs.get(CONFIG_PLATFORM);
	}
	/**
	 * @param c game system type to get configuration for
	 * @return configuration for system of type {@code c}
	 */
	public static Properties config(Class<? extends GameSystem> c) {
		return configs.get(c.getName());
	}

	/**
	 * @param c class requesting logger
	 * @return logger without any appenders for {@code c}
	 */
	public static Logger getLogger(Class<?> c) {
		return Logger.getLogger(c.getName(), Level.DEBUG, Formatters.simple());
	}

	private static String getFileName(String props) {
		return props + EXTENSION;
	}
	private static String getDefaultsFileName(String props) {
		return props + DEFAULT_EXTENSION;
	}
}
