package dev.kkorolyov.pancake.platform.animation;

import dev.kkorolyov.pancake.platform.utility.ArgVerify;

/**
 * Scrolls through a timeline using an incrementing cursor, each time returning the difference between the current timeline value and the last-accessed value.
 */
public final class Playback<T extends Frame<T>> {
	private final Timeline<T> timeline;
	private int offset;
	private T last;
	private State state = State.INITIAL;

	/**
	 * Constructs a new playback for {@code timeline}.
	 */
	public Playback(Timeline<T> timeline) {
		this.timeline = timeline;
	}

	/**
	 * Increments the current timeline cursor by {@code amount} and returns the difference between the last-accessed timeline value and the current value.
	 * If done iterating through the timeline, returns {@code null}.
	 */
	public T update(int amount) {
		if (timeline.size() < 0) {
			state = State.INITIAL;
			return null;
		} else {
			return switch (state) {
				case INITIAL -> {
					last = timeline.get(0);
					state = State.PLAYING;

					yield last;
				}
				case PLAYING -> {
					offset += amount;
					if (offset >= timeline.size()) {
						offset = timeline.size();
						state = State.DONE;
					}

					T current = timeline.get(offset);
					T difference = current.diff(last);

					last = current;

					yield difference;
				}
				case SCRUBBING -> {
					if (offset >= timeline.size()) {
						offset = timeline.size();
						state = State.DONE;
					} else {
						state = State.PLAYING;
					}
					if (last == null) {
						last = timeline.get(0);
					}

					T current = timeline.get(offset);
					T difference = current.diff(last);

					last = current;

					yield difference;
				}
				case DONE -> null;
			};
		}
	}

	/**
	 * Returns the backing timeline.
	 */
	public Timeline<T> getTimeline() {
		return timeline;
	}

	/**
	 * Returns the current offset into the backing timeline.
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * Sets the offset to use on the next call to {@link #update(int)}.
	 */
	public void setOffset(int offset) {
		this.offset = ArgVerify.betweenInclusive("offset", 0, size(), offset);
		state = State.SCRUBBING;
	}

	/**
	 * Returns the maximum supported offset of this playback.
	 */
	public int size() {
		return timeline.size();
	}

	private enum State {
		/**
		 * Offset is at {@code 0}.
		 * The next {@link #update(int)} will return the timeline {@code 0} value and start playback.
		 */
		INITIAL,
		/**
		 * Currently scrolling through the timeline.
		 * Further {@link #update(int)} calls will return further timeline values.
		 */
		PLAYING,
		/**
		 * Finished scrolling through the timeline.
		 * Further {@link #update(int)} calls will return {@code null}.
		 */
		DONE,
		/**
		 * Manual offset provided since the last {@link #update(int)}.
		 * The next {@link #update(int)} will return the delta to that value.
		 */
		SCRUBBING
	}
}
