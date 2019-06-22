package dev.kkorolyov.pancake.platform.media.audio;

import java.util.function.Consumer;

/**
 * A configurable and playable piece of audio.
 */
public interface Audio {
	/** @param volume audio volume; constrained {@code [0, 1]} */
	void setVolume(double volume);
	/** @param balance audio channel balance; constrained {@code [-1, 1]} */
	void setBalance(double balance);

	/**
	 * {@link #on(State, Runnable)} with {@code this} as {@code handler}'s argument.
	 */
	default void on(State state, Consumer<? super Audio> handler) {
		on(state, () -> handler.accept(this));
	}
	/**
	 * Registers a state change handler to this audio instance invoked when its internal state changes to {@code state}.
	 * @param state handled state
	 * @param handler operation to invoke
	 */
	void on(State state, Runnable handler);

	/**
	 * Changes the internal state of this audio instance.
	 * @param state state to set
	 */
	void state(State state);

	/**
	 * A discrete state an audio instance may be in.
	 */
	enum State {
		/** Audio is playing */
		PLAY,
		/** Audio is paused */
		PAUSE,
		/** Audio is stopped */
		STOP,
		/** Audio reached end of stream */
		END
	}
}
