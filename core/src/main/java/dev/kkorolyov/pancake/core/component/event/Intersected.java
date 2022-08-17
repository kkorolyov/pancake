package dev.kkorolyov.pancake.core.component.event;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector2;

/**
 * Notes that an intersection between entities occurred.
 * Each mutually-intersecting entity is provided the same instance of this component.
 */
public final class Intersected implements Component {
	private final Entity a, b;
	private final Vector2 mtvA, mtvB;

	/**
	 * Constructs a new intersected component between {@code a} and {@code b} with the {@code a}-relative {@code mtv}.
	 * Assigns the component to both {@code a} and {@code b}.
	 */
	public static void create(Entity a, Entity b, Vector2 mtv) {
		Intersected intersected = new Intersected(a, b, mtv);
		a.put(intersected);
		b.put(intersected);
	}

	private Intersected(Entity a, Entity b, Vector2 mtv) {
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
