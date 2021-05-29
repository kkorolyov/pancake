package dev.kkorolyov.pancake.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Provides access to configuration.
 */
public final class Config {
	private static final String EXTENSION = ".properties";
	private static final String DEFAULT_EXTENSION = ".default" + EXTENSION;

	private static final String CONFIG_PLATFORM = "pancake";

	private static final Logger LOG = LoggerFactory.getLogger(Config.class);

	private static final Map<String, Properties> configs = new HashMap<>();

	private Config() {}

	/**
	 * Clears all loaded configurations.
	 */
	public static void clear() {
		configs.clear();
	}

	/** @return platform configuration */
	public static Properties get() {
		return get(CONFIG_PLATFORM);
	}
	/**
	 * @param c game system type to get configuration for
	 * @return configuration for system of type {@code c}
	 */
	public static Properties get(Class<? extends GameSystem> c) {
		return get(c.getName());
	}
	private static Properties get(String name) {
		return configs.computeIfAbsent(name, Config::load);
	}

	private static Properties load(String name) {
		Properties config = new Properties();

		try (
				InputStream savedDefaults = Resources.inStream(getDefaultsFileName(name));
				InputStream saved = Resources.inStream(getFileName(name));
		) {
			if (savedDefaults != null) {
				config.load(savedDefaults);
				LOG.info("Reloaded default config [{}]", name);
			}
			if (saved != null) {
				config.load(saved);
				LOG.info("Reloaded config [{}]", name);
			}
		} catch (IOException e) {
			LOG.error("Failed to load config", e);
			throw new UncheckedIOException(e);
		}
		return config;
	}
	private static String getFileName(String props) {
		return props + EXTENSION;
	}
	private static String getDefaultsFileName(String props) {
		return props + DEFAULT_EXTENSION;
	}
}
