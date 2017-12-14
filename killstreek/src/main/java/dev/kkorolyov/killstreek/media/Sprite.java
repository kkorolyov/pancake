package dev.kkorolyov.killstreek.media;

import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.media.Animated;
import dev.kkorolyov.pancake.platform.media.CompositeImage;
import dev.kkorolyov.pancake.platform.media.Renderable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * A dynamic image.
 */
public class Sprite implements Renderable, Animated {
	private final CompositeImage image;
	private final Vector orientationOffset;

	private final Vector origin = new Vector();
	private final Vector frames = new Vector();
	private final Vector frameSize = new Vector();

	private float frameInterval;
	private float currentFrameTime;
	private int frame;
	private boolean active = true;

	/**
	 * Constructs a new static sprite.
	 * @see #Sprite(CompositeImage, Vector, int, int, float)
	 */
	public Sprite(CompositeImage image, Vector orientationOffset) {
		this(image, orientationOffset, 1, 1, 0);
	}
	/**
	 * Constructs a new animated sprite.
	 * @param image sprite sheet
	 * @param orientationOffset angle vector used for padding sprite orientation calculation
	 * @param xFrames number of frames in {@code image} along x-axis
	 * @param yFrames number of frames in {@code image} along y-axis
	 * @param frameInterval seconds between frame changes
	 */
	public Sprite(CompositeImage image, Vector orientationOffset, int xFrames, int yFrames, float frameInterval) {
		this.image = image;
		this.orientationOffset = orientationOffset;

		frames.set(xFrames, yFrames);
		frameSize.set(image.getSize().getX() / frames.getX(), image.getSize().getY() / frames.getY());

		setFrameInterval(frameInterval);
	}

	@Override
	public void render(GraphicsContext g, Vector position) {
		for (Image layer : image) {
			g.drawImage(layer, origin.getX(), origin.getY(), frameSize.getX(), frameSize.getY(),
					position.getX(), position.getY(), frameSize.getX(), frameSize.getY());
		}
	}

	@Override
	public Vector size() {
		return frameSize;
	}

	@Override
	public Vector getOrientationOffset() {
		return orientationOffset;
	}

	@Override
	public void tick(float dt) {
		if (!isActive()) return;

		currentFrameTime += dt;
		if (currentFrameTime < Math.abs(frameInterval)) return;

		setFrame(frame + Math.round(currentFrameTime / frameInterval));	// Reversed if negative
		currentFrameTime = 0;
	}

	@Override
	public boolean isActive() {
		return active && frameInterval != 0;
	}
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int getFrame() {
		return frame;
	}
	@Override
	public void setFrame(int frame) {
		this.frame = Math.floorMod(frame, length());

		origin.set((int) (this.frame % frames.getX()) * frameSize.getX(),
				(int) (this.frame / frames.getX()) * frameSize.getY());
	}

	@Override
	public float getFrameInterval() {
		return frameInterval;
	}
	@Override
	public void setFrameInterval(float frameInterval) {
		this.frameInterval = frameInterval;
	}

	@Override
	public int length() {
		return (int) (frames.getX() * frames.getY());
	}

	@Override
	public String toString() {
		return "Sprite{" +
				"image=" + image +
				", origin=" + origin +
				", frames=" + frames +
				", frameSize=" + frameSize +
				", frameInterval=" + frameInterval +
				", currentFrameTime=" + currentFrameTime +
				", frame=" + frame +
				", active=" + active +
				'}';
	}
}
