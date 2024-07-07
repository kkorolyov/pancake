package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.core.animation.TransformFrame;
import dev.kkorolyov.pancake.platform.animation.Frame;
import dev.kkorolyov.pancake.platform.animation.Playback;
import dev.kkorolyov.pancake.platform.animation.Timeline;
import dev.kkorolyov.pancake.platform.entity.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Emits {@link TransformFrame} diffs according to the current state.
 */
public final class AnimationQueue implements Component, Iterable<AnimationQueue.PlaybackConfig<TransformFrame>> {
	private final Collection<PlaybackConfig<TransformFrame>> timelines = new ArrayList<>();
	private boolean active = true;
	private long counter;

	/**
	 * Adds animation {@code timeline} to this component, and preps it for {@code type} playback.
	 * {@code timeline} offsets are expected to be in {@code ms}.
	 */
	public void add(Timeline<TransformFrame> timeline, Type type) {
		timelines.add(new PlaybackConfig<>(new Playback<>(timeline), type));
	}

	/**
	 * Increments all playbacks by {@code dt} elapsed {@code ns} since the last call and returns the sum of each playback's frame difference.
	 * Return {@code null} if all playbacks return {@code null}.
	 * Does nothing and returns {@code null} if not {@link #isActive()}.
	 */
	public TransformFrame update(long dt) {
		TransformFrame result = null;

		if (active) {
			long nsMs = (long) 1e6;

			counter += dt;

			int ms = (int) (counter / nsMs);

			var it = timelines.iterator();

			while (it.hasNext()) {
				var config = it.next();
				var partial = config.playback().update(ms);

				if (partial == null) {
					switch (config.type()) {
						case ONCE -> {
							it.remove();
						}
						case RESET -> {
							config.playback().setOffset(0);
							partial = config.playback().update(0);

							it.remove();
						}
						case LOOP -> {
							config.playback().setOffset(0);
							partial = config.playback().update(0);
						}
					}
				}

				result = (result == null) ? partial : (partial == null) ? result : result.sum(partial);
			}

			counter %= nsMs;
		}

		return result;
	}
	/**
	 * Resets all playbacks to their respective starts, and returns the sum of each playback's frame difference.
	 * Returns {@code null} if all playbacks return {@code null}.
	 */
	// TODO does this have a use-case?
	// TODO perhaps provide a setOffset API
	public TransformFrame reset() {
		TransformFrame result = null;

		for (var config : timelines) {
			config.playback().setOffset(0);
			var partial = config.playback().update(0);

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
	 * Returns the current active state.
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * Sets the current `active` state.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public Iterator<PlaybackConfig<TransformFrame>> iterator() {
		return timelines.iterator();
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

	/**
	 * Higher-level playback state maintained by an animation queue.
	 */
	public record PlaybackConfig<T extends Frame<T>>(Playback<T> playback, Type type) {}
}
