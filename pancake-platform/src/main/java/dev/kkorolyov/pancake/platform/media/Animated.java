package dev.kkorolyov.pancake.platform.media;

/**
 * Loops through a set of frames.
 */
public interface Animated {
	/**
	 * If active, proceeds to the next frame if {@code dt + } seconds since the previous frame change is {@code >= abs(frameInterval)}.
	 * @param dt seconds elapsed since previous invocation of this method
	 */
	void tick(float dt);

	/** @return whether {@link #tick(float)} is enabled */
	boolean isActive();
	/** @param active {@code true} enables {@link #tick(float)}, {@code false} disables */
	void setActive(boolean active);

	/** @return current frame number */
	int getFrame();
	/** @param frame frame number to jump to */
	void setFrame(int frame);

	/** @return seconds between frame changes, negative means the animation runs in reverse */
	float getFrameInterval();
	/** @param frameInterval seconds between frame changes, may be negative to run in reverse */
	void setFrameInterval(float frameInterval);
	/** @return total number of animation frames */

	/** @return total number of frames */
	int length();
}
