package dev.kkorolyov.pancake.core.component.tag;

import dev.kkorolyov.pancake.platform.entity.Component;

import java.util.Objects;

/**
 * A marker indicating that the owning entity can respond to collisions.
 * Ordered by increasing priority value.
 */
public final class Collidable implements Component, Comparable<Collidable> {
	private final int priority;

	/**
	 * Constructs a collidable of {@code 0} priority.
	 */
	public Collidable() {
		this(0);
	}
	/**
	 * Constructs a collidable of {@code priority}.
	 */
	public Collidable(int priority) {
		this.priority = priority;
	}

	/**
	 * Returns the collision priority of this component.
	 */
	public int getPriority() {
		return priority;
	}

	@Override
	public int compareTo(Collidable o) {
		return Integer.compare(priority, o.priority);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Collidable other)) return false;
		return priority == other.priority;
	}
	@Override
	public int hashCode() {
		return Objects.hashCode(priority);
	}
}
