package dev.kkorolyov.pancake.platform.media;

/**
 * Loops through a set of frames.
 */
public interface Animated {
	/**
	 * If active, proceeds to the next frame if {@code dt + ns} since the previous frame change is {@code >= abs(frameInterval)}.
	 * @param dt {@code ns} elapsed since previous invocation of this method
	 */
	void tick(long dt);

	/**
	 * Toggles active status.
	 */
	void toggle();

	/** @return whether {@link #tick(long)} is enabled */
	boolean isActive();
	/** @param active {@code true} enables {@link #tick(long)}, {@code false} disables */
	void setActive(boolean active);

	/** @return current frame number */
	int getFrame();
	/** @param frame frame number to jump to */
	void setFrame(int frame);

	/** @return {@code ns} between frame changes, negative means the animation runs in reverse */
	long getFrameInterval();
	/** @param frameInterval {@code ns} between frame changes, may be negative to run in reverse */
	void setFrameInterval(long frameInterval);

	/** @return total number of frames */
	int length();
}
