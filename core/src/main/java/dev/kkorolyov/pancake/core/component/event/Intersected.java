package dev.kkorolyov.pancake.core.component.event;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Notes intersections that have occurred between this entity and others.
 * Instances of this component can only be created opaquely through {@link #assign(Entity, Entity, Vector2)}.
 */
public final class Intersected implements Component, Iterable<Intersected.Event> {
	private final List<Event> events = new ArrayList<>();

	/**
	 * Constructs a new intersected event between {@code a} and {@code b} with the {@code a}-relative {@code mtv}.
	 * Assigns the event to both {@code a} and {@code b}.
	 */
	public static void assign(Entity a, Entity b, Vector2 mtv) {
		Event event = new Event(a, b, mtv);
		add(a, event);
		add(b, event);
	}
	private static void add(Entity entity, Event event) {
		var intersected = entity.get(Intersected.class);
		if (intersected == null) {
			intersected = new Intersected();
			entity.put(intersected);
		}
		intersected.add(event);
	}

	private Intersected() {}

	/**
	 * Adds {@code event} to the list of intersection events.
	 */
	public void add(Event event) {
		events.add(event);
	}

	/**
	 * Returns an iterator over all intersection events.
	 */
	@Override
	public Iterator<Event> iterator() {
		return events.iterator();
	}

	/**
	 * A distinct intersection event between 2 entities.
	 * Each of the entities is provided the same instance of this event.
	 */
	public static final class Event {
		private final Entity a, b;
		private final Vector2 mtvA, mtvB;

		private Event(Entity a, Entity b, Vector2 mtv) {
			this.a = a;
			this.b = b;

			mtvA = Vector2.of(mtv);
			mtvB = Vector2.of(mtv);
			mtvB.scale(-1);
		}

		/**
		 * Returns the first intersecting entity.
		 */
		public Entity getA() {
			return a;
		}
		/**
		 * Returns the second intersecting entity.
		 */
		public Entity getB() {
			return b;
		}

		/**
		 * Returns the {@link #getA()} or {@link #getB()} entity that is not {@code entity}.
		 * Throws {@link IllegalArgumentException} if {@code entity} is neither of the entities of this event.
		 */
		public Entity getOther(Entity entity) {
			if (entity.equals(b)) return a;
			else if (entity.equals(a)) return b;
			else throw new IllegalArgumentException("is neither of the intersecting entities: [" + entity.getId() + "]");
		}

		/**
		 * Returns the {@link #getMtvA()} or {@link #getMtvB()} matching {@code entity}.
		 * Throws {@link IllegalArgumentException} if {@code entity} is neither of the entities of this event.
		 */
		public Vector2 getMtv(Entity entity) {
			if (entity.equals(a)) return mtvA;
			else if (entity.equals(b)) return mtvB;
			else throw new IllegalArgumentException("is neither of the intersecting entities: [" + entity.getId() + "]");
		}
		/**
		 * Returns the {@link #getMtvA()} or {@link #getMtvB()} matching the entity that is not {@code entity}.
		 * Throws {@link IllegalArgumentException} if {@code entity} is neither of the entities of this event.
		 */
		public Vector2 getOtherMtv(Entity entity) {
			if (entity.equals(b)) return mtvA;
			else if (entity.equals(a)) return mtvB;
			else throw new IllegalArgumentException("is neither of the intersecting entities: [" + entity.getId() + "]");
		}

		/**
		 * Returns the {@link #getA()}-relative minimum translation vector to separate it from {@link #getB()}.
		 */
		public Vector2 getMtvA() {
			return mtvA;
		}
		/**
		 * Returns the {@link #getB()}-relative minimum translation vector to separate it from {@link #getA()}.
		 */
		public Vector2 getMtvB() {
			return mtvB;
		}
	}
}
