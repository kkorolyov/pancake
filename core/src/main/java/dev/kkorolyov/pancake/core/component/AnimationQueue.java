package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.core.animation.TransformFrame;
import dev.kkorolyov.pancake.platform.animation.Frame;
import dev.kkorolyov.pancake.platform.animation.Playback;
import dev.kkorolyov.pancake.platform.animation.Timeline;
import dev.kkorolyov.pancake.platform.entity.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Emits {@link TransformFrame} diffs according to the current state.
 */
public final class AnimationQueue implements Component {
	private final Map<Timeline<TransformFrame>, PlaybackConfig<TransformFrame>> timelines = new HashMap<>();
	private long counter;

	/**
	 * Adds animation {@code timeline} to this component, if it is not already added, and preps it for {@code type} playback.
	 * {@code timeline} offsets are expected to be in {@code ms}.
	 */
	public void add(Timeline<TransformFrame> timeline, Type type) {
		timelines.computeIfAbsent(timeline, k -> new PlaybackConfig<>(new Playback<>(k), type));
	}

	/**
	 * Increments all playbacks by {@code dt} elapsed {@code ns} since the last call and returns the sum of each playback's frame difference.
	 * Return {@code null} if all playbacks return {@code null}.
	 */
	public TransformFrame update(long dt) {
		long nsMs = (long) 1e6;

		counter += dt;

		int ms = (int) (counter / nsMs);

		var it = timelines.values().iterator();

		TransformFrame result = null;

		while (it.hasNext()) {
			var config = it.next();
			var partial = config.playback().update(ms);

			if (partial == null) {
				switch (config.type()) {
					case ONCE -> {
						it.remove();
					}
					case RESET -> {
						partial = config.playback().reset();

						it.remove();
					}
					case LOOP -> {
						partial = config.playback().reset();
					}
				}
			}

			result = (result == null) ? partial : (partial == null) ? result : result.sum(partial);
		}

		counter %= nsMs;

		return result;
	}
	/**
	 * Resets all playbacks to their respective starts, and returns the sum of each playback's frame difference.
	 * Returns {@code null} if all playbacks return {@code null}.
	 */
	public TransformFrame reset() {
		TransformFrame result = null;

		for (var config : timelines.values()) {
			var partial = config.playback().reset();

			result = (result == null) ? partial : (partial == null) ? result : result.sum(partial);
		}

		return result;
	}
	/**
	 * Removes all playbacks immediately without any additional termination / cleanup.
	 */
	public void clear() {
		timelines.clear();
	}

	/**
	 * Controls the {@link Playback} behavior of a {@link Timeline}.
	 */
	public enum Type {
		/**
		 * Plays through once to the end of the timeline, then removes the timeline from this animation.
		 */
		ONCE,
		/**
		 * Plays through once to the end of the timeline, then resets to the first frame of the timeline, then removes the timeline from this animation.
		 */
		RESET,
		/**
		 * Continuously repeats the timeline from the first frame upon reaching the last frame.
		 */
		LOOP
	}

	private record PlaybackConfig<T extends Frame<T>>(Playback<T> playback, Type type) {}
}
