package dev.kkorolyov.pancake.platform.animation;

import java.util.Objects;

/**
 * Seeks through a timeline, returning the difference between the current timeline value and the last-accessed value.
 */
public final class Playback<T extends Frame<T>> {
	private final Timeline<T> timeline;
	private int offset;
	private T last;

	/**
	 * Constructs a new playback for {@code timeline}.
	 */
	public Playback(Timeline<T> timeline) {
		this.timeline = timeline;
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
	 * Sets the {@code offset}, clamped to {@code [0, max(0, this.size())]}.
	 * Returns the difference between the last-accessed timeline value and the value at the clamped {@code offset}.
	 * If the backing timeline is empty, returns {@code null}.
	 */
	public T setOffset(int offset) {
		if (timeline.size() < 0) {
			this.offset = 0;
			last = null;
			return last;
		} else if (last == null) {
			this.offset = Math.max(0, Math.min(timeline.size(), offset));

			last = timeline.get(this.offset);
			return last;
		} else {
			this.offset = Math.max(0, Math.min(timeline.size(), offset));

			T current = timeline.get(this.offset);
			T difference = current.diff(last);

			last = current;
			return difference;
		}
	}

	/**
	 * Returns the maximum supported offset of this playback.
	 */
	public int size() {
		return timeline.size();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Playback<?> other)) return false;
		return offset == other.offset && Objects.equals(timeline, other.timeline) && Objects.equals(last, other.last);
	}
	@Override
	public int hashCode() {
		return Objects.hash(timeline, offset, last);
	}
}
