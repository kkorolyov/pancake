package dev.kkorolyov.pancake.platform.animation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A map of role keys assigned to timelines, like a stateless animation spec.
 */
public final class Choreography<T extends Frame<T>> implements Iterable<Choreography.Role<T>> {
	private final Map<String, Role<T>> roles = new HashMap<>();

	/**
	 * Assigns role {@code key} to {@code timeline}.
	 */
	public void put(String key, Timeline<T> timeline) {
		roles.put(key, new Role<>(key, timeline, this));
	}

	/**
	 * Returns the role for {@code key}, if any.
	 */
	public Role<T> get(String key) {
		return roles.get(key);
	}

	/**
	 * Returns an iterator over all roles in this.
	 */
	@Override
	public Iterator<Role<T>> iterator() {
		return roles.values().iterator();
	}

	/**
	 * A distinct {@code (key, timeline)} assignment of a particular {@code choreography}.
	 */
	public record Role<T extends Frame<T>>(String key, Timeline<T> timeline, Choreography<T> choreography) {}
}
