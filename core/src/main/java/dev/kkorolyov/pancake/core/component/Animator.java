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
	private long offset;

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
	 * Returns {@code null} if all playbacks return {@code null}.
	 * Does nothing and returns {@code null} if not {@link #isActive()}.
	 */
	public TransformFrame update(long dt) {
		TransformFrame result = null;

		if (active) {
			long nsMs = (long) 1e6;

			offset += dt;

			int offsetMs = (int) (offset / nsMs);

			var it = configs.values().iterator();

			while (it.hasNext()) {
				var config = it.next();
				var partial = config.playback().setOffset(offsetMs);

				if (partial == null && config.playback().size() >= 0) {
					switch (config.type()) {
						case ONCE -> {
							it.remove();
						}
						case RESET -> {
							config.playback().setOffset(0);
							partial = config.playback().setOffset(0);

							it.remove();
						}
						case LOOP -> {
							config.playback().setOffset(0);
							partial = config.playback().setOffset(0);
						}
					}
				}

				result = (result == null) ? partial : (partial == null) ? result : result.sum(partial);
			}

			var modulus = duration() * nsMs;
			if (modulus > 0) offset %= modulus;
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

	public int getOffset() {
		return (int) (offset / 1e6);
	}
	public void setOffset(int offset) {
		this.offset = (long) (offset * 1e6);
	}

	/**
	 * Returns the number of assigned roles.
	 */
	public int size() {
		return configs.size();
	}
	/**
	 * Returns the duration of the longest assigned role.
	 */
	public int duration() {
		var result = 0;
		for (PlaybackConfig<TransformFrame> playbackConfig : configs.values()) {
			result = Math.max(result, playbackConfig.playback.size());
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Animator other)) return false;
		return active == other.active && offset == other.offset && Objects.equals(configs, other.configs);
	}
	@Override
	public int hashCode() {
		return Objects.hash(configs, active, offset);
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
