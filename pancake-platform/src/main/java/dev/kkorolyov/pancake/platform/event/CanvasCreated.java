package dev.kkorolyov.pancake.platform.event;

import javafx.scene.canvas.Canvas;

/**
 * {@link Event} broadcast when a {@link Canvas} is created.
 */
public class CanvasCreated implements Event {
	private final Canvas canvas;

	/**
	 * Constructs a new canvas created event.
	 * @param canvas created canvas
	 */
	public CanvasCreated(Canvas canvas) {
		this.canvas = canvas;
	}

	/** @return created canvas */
	public Canvas getCanvas() {
		return canvas;
	}
}
