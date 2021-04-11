package dev.kkorolyov.pancake.platform.application;

import dev.kkorolyov.pancake.platform.GameLoop;
import dev.kkorolyov.pancake.platform.math.Vector2;

import java.util.Collection;

/**
 * Provides for interactions with the service running the {@link GameLoop}.
 */
public interface Application {
	/**
	 * Transforms a generic input key into an enum describing that input.
	 * @param key input key
	 * @return enum describing input identified by {@code key}
	 * @throws IllegalArgumentException if no input found for {@code key}
	 */
	Enum<?> toInput(String key);

	/** @return current cursor position in {@code px} */
	Vector2 getCursor();
	/** @return current active inputs */
	Collection<Enum<?>> getInputs();

	/**
	 * Launches an application service according to a configuration and game loop.
	 * @param config configuration to use
	 * @param gameLoop game loop to execute
	 */
	void execute(Config config, GameLoop gameLoop);

	/**
	 * Configuration for running applications.
	 */
	class Config {
		/** Application title */
		public final String title;
		/** URI to application icon */
		public final String iconUri;
		/** Application window width in {@code px} */
		public final double width;
		/** Application window width in {@code px} */
		public final double height;

		/**
		 * Constructs a new application configuration.
		 * @param title application title
		 * @param iconUri uri to application icon
		 * @param width application window width in {@code px}
		 * @param height application window height in {@code px}
		 */
		public Config(String title, String iconUri, double width, double height) {
			this.title = title;
			this.iconUri = iconUri;
			this.width = width;
			this.height = height;
		}
	}
}
