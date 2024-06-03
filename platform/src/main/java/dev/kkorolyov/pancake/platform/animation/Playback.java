package dev.kkorolyov.pancake.platform.animation;

/**
 * Scrolls through a timeline using an incrementing cursor, each time returning the difference between the current timeline value and the last-accessed value.
 */
public final class Playback<T extends Frame<T>> {
	private final Timeline<T> timeline;
	private int cursor;
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
		return switch (state) {
			case INITIAL -> {
				last = timeline.get(0);
				state = State.PLAYING;

				yield last;
			}
			case PLAYING -> {
				cursor += amount;
				if (cursor >= timeline.size()) {
					cursor = timeline.size();
					state = State.DONE;
				}

				T current = timeline.get(cursor);
				T difference = current.diff(last);

				last = current;

				yield difference;
			}
			case DONE -> null;
		};
	}

	/**
	 * Returns the difference between the last-accessed timeline value and the timeline {@code 0} value, and resets the playback cursor to {@code 0}.
	 * If {@link #update(int)} has not yet been called to start playback, simply returns {@code null}.
	 */
	public T reset() {
		if (state == State.INITIAL) {
			return null;
		} else {
			T first = timeline.get(0);
			T difference = first.diff(last);

			cursor = 0;
			last = first;
			state = State.PLAYING;

			return difference;
		}
	}

	private enum State {
		/**
		 * Cursor is at {@code 0}.
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
		DONE
	}
}
