package dev.kkorolyov.pancake.platform.event;

import javafx.scene.Scene;

/**
 * {@link Event} broadcast when a {@link Scene} is created.
 */
public class SceneCreated implements Event {
	private final Scene scene;

	/**
	 * Constructs a new scene created event.
	 * @param scene created scene
	 */
	public SceneCreated(Scene scene) {
		this.scene = scene;
	}

	/** @return created scene */
	public Scene getScene() {
		return scene;
	}
}
