package dev.kkorolyov.pancake.core.component.tag;

import dev.kkorolyov.pancake.platform.entity.Component;

import java.util.Objects;

/**
 * A marker indicating that the owning entity can respond to boundary intersections.
 * Ordered by increasing priority value.
 */
public final class Correctable implements Component, Comparable<Correctable> {
	private final int priority;

	/**
	 * Constructs a correctable of {@code 0} priority.
	 */
	public Correctable() {
		this(0);
	}
	/**
	 * Constructs a correctable of {@code priority}.
	 */
	public Correctable(int priority) {
		this.priority = priority;
	}

	@Override
	public int compareTo(Correctable o) {
		return Integer.compare(priority, o.priority);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Correctable o = (Correctable) obj;
		return priority == o.priority;
	}
	@Override
	public int hashCode() {
		return Objects.hash(priority);
	}
}
