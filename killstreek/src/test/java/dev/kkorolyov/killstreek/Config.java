package dev.kkorolyov.killstreek;

import dev.kkorolyov.simplefiles.stream.StreamStrategies;
import dev.kkorolyov.simpleprops.Properties;

import static dev.kkorolyov.simplefiles.Files.in;

public final class Config {
	public static final Properties images = new Properties(in("config/images", StreamStrategies.IN_PATH, StreamStrategies.IN_CLASSPATH));
	public static final Properties sounds = new Properties(in("config/sounds", StreamStrategies.IN_PATH, StreamStrategies.IN_CLASSPATH));

	private Config() {}
}
