package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.event.EventBroadcaster;
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter;

import java.util.Objects;

/**
 * A container of {@link GameEngine} resources.
 */
public final class SharedResources {
	public final EventBroadcaster events;
	public final PerformanceCounter performanceCounter;

	/**
	 * Constructs a new resources container.
	 * @param events shared event broadcaster
	 * @param performanceCounter shared performance counter
	 */
	public SharedResources(EventBroadcaster events, PerformanceCounter performanceCounter) {
		this.events = events;
		this.performanceCounter = performanceCounter;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SharedResources sharedResources = (SharedResources) o;
		return Objects.equals(events, sharedResources.events) &&
				Objects.equals(performanceCounter, sharedResources.performanceCounter);
	}
	@Override
	public int hashCode() {
		return Objects.hash(events, performanceCounter);
	}
}
