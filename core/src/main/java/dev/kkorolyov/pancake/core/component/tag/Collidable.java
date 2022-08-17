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

	@Override
	public int compareTo(Collidable o) {
		return Integer.compare(priority, o.priority);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Collidable o = (Collidable) obj;
		return priority == o.priority;
	}
	@Override
	public int hashCode() {
		return Objects.hash(priority);
	}
}
