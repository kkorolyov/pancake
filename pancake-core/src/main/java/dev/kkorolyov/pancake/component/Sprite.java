package dev.kkorolyov.pancake.component;

import dev.kkorolyov.pancake.Component;
import dev.kkorolyov.pancake.math.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * A dynamic image.
 */
public class Sprite implements Component {
	private final Image baseImage;
	private final Vector origin;
	private final Vector size;
	private float tickInterval;
	private float sinceLastTick;
	private boolean frozen;

	/**
	 * Constructs a new static sprite.
	 * @param baseImage sprite image
	 */
	public Sprite(Image baseImage) {
		this(baseImage, (float) baseImage.getWidth(), (float) baseImage.getHeight(), 0);

		frozen = true;
	}
	/**
	 * Constructs a new animated sprite.
	 * @param baseImage base sprite sheet
	 * @param width width in px of a single sprite
	 * @param height height in px of a single sprite
	 * @param tickInterval seconds between frame changes
	 */
	public Sprite(Image baseImage, float width, float height, float tickInterval) {
		this.baseImage = baseImage;
		this.tickInterval = tickInterval;

		origin = new Vector(0, 0, 0);
		size = new Vector(width, height);
	}

	/**
	 * Advances this sprite's animation by 1 frame.
	 * If this sprite is already on the final frame, it loops back to the initial frame.
	 * If this sprite is frozen or has only 1 frame, this method does nothing.
	 * @param dt seconds elapsed since last invocation of this method
	 * @see #freeze(boolean)
	 */
	public void tick(float dt) {
		if (frozen) return;

		sinceLastTick += dt;
		if (sinceLastTick >= tickInterval) {
			for (int i = 0; i < (int) (sinceLastTick / tickInterval); i++) {  // Catch up on skipped ticks, if applicable
				if ((origin.getX() + size.getX()) >= baseImage.getWidth()) {  // Right edge of viewport
					if ((origin.getY() + size.getY()) >= baseImage.getHeight()) {  // Bottom edge of viewport
						origin.set(0, 0);  // Wrap to start
					} else {  // Next line
						origin.set(0, origin.getY() + size.getY());
					}
				} else {
					origin.translate(size.getX(), 0);
				}
			}
			sinceLastTick = 0;
		}
	}

	/**
	 * Draws this sprite's current frame.
	 * @param g drawing context
	 * @param position draw position
	 */
	public void draw(GraphicsContext g, Vector position) {
		g.drawImage(baseImage, origin.getX(), origin.getY(), size.getX(), size.getY(),
								position.getX() - size.getX() / 2, position.getY() - size.getY() / 2, size.getX(), size.getY());
	}

	/** @return entire base image */
	public Image getBaseImage() {
		return baseImage;
	}

	/** @param frozen {@code true} disables all {@link #tick(float)} calls; {@code false} enables */
	public void freeze(boolean frozen) {
		this.frozen = frozen;
	}

	@Override
	public String toString() {
		return "Sprite{" +
					 "baseImage=" + baseImage +
					 ", origin=" + origin +
					 ", size=" + size +
					 '}';
	}
}
