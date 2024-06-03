package dev.kkorolyov.pancake.platform.animation;

/**
 * A distinct point in an animation.
 * Also supports usage as a delta or an additive combination of frames.
 */
public interface Frame<T> {
	/**
	 * Returns the {@code mix}ed linear interpolation between {@code this} and {@code other}.
	 * i.e. returns a frame that is {@code 1 - mix} portion {@code this} and {@code mix} portion {@code other}.
	 */
	T lerp(T other, double mix);

	/**
	 * Returns the summation of {@code this} and {@code other}.
	 * i.e. returns a frame that is {@code this + other}
	 */
	T sum(T other);

	/**
	 * Returns the difference of {@code this} and {@code other}.
	 * i.e. returns a frame that is {@code this - other}
	 */
	T diff(T other);
}
