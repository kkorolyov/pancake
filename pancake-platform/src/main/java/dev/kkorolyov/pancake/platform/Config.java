package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.flopple.function.throwing.ThrowingConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.stream.Stream;

/**
 * Provides access to configuration.
 */
public final class Config {
	private static final String EXTENSION = ".properties";
	private static final String DEFAULT_EXTENSION = ".default" + EXTENSION;

	private static final String CONFIG_PLATFORM = "pancake";

	private static final Logger LOG = LoggerFactory.getLogger(Config.class);

	private static final Map<String, Properties> configs = new HashMap<>();

	static {
		reload();
	}

	private Config() {}

	/**
	 * Reloads the platform configuration and all system configurations.
	 */
	public static void reload() {
		Stream.concat(
				Stream.of(CONFIG_PLATFORM),
				ServiceLoader.load(GameSystem.class).stream()
						.map(ServiceLoader.Provider::type)
						.map(Class::getName)
		).forEach(Config::reload);
	}
	private static void reload(String name) {
		Properties config = get(name);
		config.clear();

		Resources.inStream(getDefaultsFileName(name)).ifPresent((ThrowingConsumer<? super InputStream, ?>) in -> {
			config.load(in);
			LOG.info("Reloaded default config [{}]", name);
		});
		Resources.inStream(getFileName(name)).ifPresent((ThrowingConsumer<? super InputStream, ?>) in -> {
			config.load(in);
			LOG.info("Reloaded config [{}]", name);
		});
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
		return configs.computeIfAbsent(name, k -> new Properties());
	}

	private static String getFileName(String props) {
		return props + EXTENSION;
	}
	private static String getDefaultsFileName(String props) {
		return props + DEFAULT_EXTENSION;
	}
}
