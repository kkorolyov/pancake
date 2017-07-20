package dev.kkorolyov.pancake.component;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.graphics.CompositeImage;
import dev.kkorolyov.pancake.math.Vector;

/**
 * A dynamic image.
 */
public class Sprite implements Component {
	private CompositeImage image;

	private final Vector origin = new Vector();
	private final Vector frames = new Vector();
	private final Vector frameSize = new Vector();

	private float frameInterval;
	private float currentFrameTime;
	private int frame;
	private boolean stopped;

	/**
	 * Constructs a new static sprite.
	 * @param image sprite image
	 */
	public Sprite(CompositeImage image) {
		this(image, 1, 1, 0);
	}
	/**
	 * Constructs a new animated sprite.
	 * @param image sprite sheet
	 * @param xFrames number of frames in {@code image} along x-axis
	 * @param yFrames number of frames in {@code image} along y-axis
	 * @param frameInterval seconds between frame changes
	 */
	public Sprite(CompositeImage image, int xFrames, int yFrames, float frameInterval) {
		setImage(image);
		setFrames(xFrames, yFrames);
		setFrameInterval(frameInterval);
	}

	/**
	 * Changes to the next frame, if enough time has elapsed.
	 * @param dt seconds elapsed since previous invocation of this method
	 */
	public void update(float dt) {
		if (isStopped()) return;

		currentFrameTime += dt;
		if (currentFrameTime < Math.abs(frameInterval)) return;

		setFrame(frame + (int) (currentFrameTime / frameInterval));	// Reversed if negative
		currentFrameTime = 0;
	}

	/** @return {@code true} if {@link #update(float)} is disabled */
	public boolean isStopped() {
		return stopped || frameInterval == 0;
	}
	/**
	 * @param stopped {@code true} disables {@link #update(float)}, {@code false} enables
	 * @param reset if {@code true}, sets the current frame to {@code 0}
	 */
	public void stop(boolean stopped, boolean reset) {
		this.stopped = stopped;

		if (reset) setFrame(0);
	}

	/** @return coordinate of upper-left corner of the current frame */
	public Vector getOrigin() {
		return origin;
	}
	/** @return dimensions of a single frame */
	public Vector getSize() {
		return frameSize;
	}

	/** @return sprite image */
	public CompositeImage getImage() {
		return image;
	}

	/** @param image new sprite image */
	public void setImage(CompositeImage image) {
		this.image = image;

		applyFrameSize();
	}
	/** @return index of current frame, starting from {@code 0} */
	public int getFrame() {
		return frame;
	}

	/** @param frame new current frame */
	public void setFrame(int frame) {
		this.frame = Math.floorMod(frame, getLength());

		origin.set((int) (this.frame % frames.getX()) * frameSize.getX(),
							 (int) (this.frame / frames.getX()) * frameSize.getY());
	}

	/** @return number of frames along x and y axes on this sprite's image */
	public Vector getFrames() {
		return frames;
	}

	/**
	 * @param xFrames number of frames along x-axis on this sprite's image
	 * @param yFrames number of frames along y-axis on this sprite's image
	 */
	public void setFrames(int xFrames, int yFrames) {
		frames.set(xFrames, yFrames);

		applyFrameSize();
	}

	/** @return total number of frames */
	public int getLength() {
		return (int) (frames.getX() * frames.getY());
	}

	private void applyFrameSize() {
		frameSize.set(image.getSize().getX() / frames.getX(), image.getSize().getY() / frames.getY());
	}

	/** @return seconds between frame changes */
	public float getFrameInterval() {
		return frameInterval;
	}

	/** @param frameInterval new seconds between frame changes */
	public void setFrameInterval(float frameInterval) {
		this.frameInterval = frameInterval;
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
					 ", stopped=" + stopped +
					 '}';
	}
}
