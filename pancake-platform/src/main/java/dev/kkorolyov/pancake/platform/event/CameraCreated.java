package dev.kkorolyov.pancake.platform.event;

import dev.kkorolyov.pancake.platform.media.Camera;

/**
 * {@link Event} broadcast when a {@link Camera} is created.
 */
public class CameraCreated implements Event {
	private final Camera camera;

	/**
	 * Constructs a new camera created event.
	 * @param camera created camera
	 */
	public CameraCreated(Camera camera) {
		this.camera = camera;
	}

	/** @return created camera */
	public Camera getCamera() {
		return camera;
	}
}
