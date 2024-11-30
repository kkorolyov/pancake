package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.core.animation.TransformFrame;
import dev.kkorolyov.pancake.platform.animation.Choreography;
import dev.kkorolyov.pancake.platform.animation.Frame;
import dev.kkorolyov.pancake.platform.animation.Playback;
import dev.kkorolyov.pancake.platform.animation.Timeline;
import dev.kkorolyov.pancake.platform.entity.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Emits a cumulative {@link TransformFrame} delta between the previous and current frames of each assigned {@link Choreography.Role}.
 * Expects an assigned role's timeline offset to be in {@code ms}.
 */
public final class Animator implements Component, Iterable<Map.Entry<Choreography.Role<TransformFrame>, Animator.PlaybackConfig<TransformFrame>>> {
	private final Map<Choreography.Role<TransformFrame>, PlaybackConfig<TransformFrame>> configs = new HashMap<>();
	private boolean active = true;
	private long counter;

	/**
	 * {@link #put(Choreography.Role, Type, int)} without setting initial offset.
	 */
	public void put(Choreography.Role<TransformFrame> role, Type type) {
		put(role, type, 0);
	}
	/**
	 * Adds animation {@code role} to this component if it is not yet set, and preps it for {@code type} playback starting at {@code offset}.
	 */
	public void put(Choreography.Role<TransformFrame> role, Type type, int offset) {
		configs.computeIfAbsent(
				role,
				k -> {
					var playback = new Playback<>(k.timeline());
					if (offset > 0) playback.setOffset(offset);

					return new PlaybackConfig<>(playback, type);
				}
		);
	}

	/**
	 * Increments all playbacks by {@code dt} elapsed {@code ns} since the last call and returns the sum of each playback's frame delta.
	 * Return {@code null} if all playbacks return {@code null}.
	 * Does nothing and returns {@code null} if not {@link #isActive()}.
	 */
	public TransformFrame update(long dt) {
		TransformFrame result = null;

		if (active) {
			long nsMs = (long) 1e6;

			counter += dt;

			int ms = (int) (counter / nsMs);

			var it = configs.values().iterator();

			while (it.hasNext()) {
				var config = it.next();
				var partial = config.playback().update(ms);

				if (partial == null && config.playback().size() >= 0) {
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

		for (var config : configs.values()) {
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
		configs.clear();
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

	/**
	 * Returns the number of assigned roles.
	 */
	public int size() {
		return configs.size();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Animator other)) return false;
		return active == other.active && counter == other.counter && Objects.equals(configs, other.configs);
	}
	@Override
	public int hashCode() {
		return Objects.hash(configs, active, counter);
	}

	/**
	 * Returns an iterator over assigned roles mapped to current playback states.
	 */
	@Override
	public Iterator<Map.Entry<Choreography.Role<TransformFrame>, Animator.PlaybackConfig<TransformFrame>>> iterator() {
		return configs.entrySet().iterator();
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
	 * Higher-level playback state maintained by an animator.
	 */
	public record PlaybackConfig<T extends Frame<T>>(Playback<T> playback, Type type) {}
}
