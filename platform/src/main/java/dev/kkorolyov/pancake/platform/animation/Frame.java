package dev.kkorolyov.pancake.platform.animation;

/**
 * A concrete point in a timeline.
 * Supports addition, difference, and interpolation with other same-type frames.
 */
public interface Frame<T extends Frame<T>> {
	/**
	 * Returns the {@code mix}ed linear interpolation from {@code other} to {@code this}.
	 * i.e. returns a frame that is {@code mix} portion {@code this} and {@code 1 - mix} portion {@code other}.
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
