package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.event.Intersected;
import dev.kkorolyov.pancake.core.component.movement.Mass;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.core.component.tag.Collidable;
import dev.kkorolyov.pancake.core.component.tag.Correctable;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Collection;
import java.util.HashSet;

/**
 * Simmulates collisions for {@link Collidable} {@link Intersected} entities.
 */
public final class CollisionSystem extends GameSystem {
	private final Vector3 vTemp = Vector3.of(0, 0, 0);
	private final Vector3 vDiff = Vector3.of(0, 0, 0);
	private final Vector3 sDiff = Vector3.of(0, 0, 0);

	private final Collection<Intersected> events = new HashSet<>();

	/**
	 * Constructs a new collision system.
	 */
	public CollisionSystem() {
		super(Intersected.class, Correctable.class, Velocity.class);
	}

	@Override
	public void update(Entity entity, long dt) {
		Intersected event = entity.get(Intersected.class);
		if (events.add(event)) {
			boolean aCollidable = event.getA().get(Collidable.class) != null;
			boolean bCollidable = event.getB().get(Collidable.class) != null;

			Transform aTransform = event.getA().get(Transform.class);
			Transform bTransform = event.getB().get(Transform.class);

			Velocity aVelocity = event.getA().get(Velocity.class);
			Velocity bVelocity = event.getB().get(Velocity.class);

			Mass aMass = event.getA().get(Mass.class);
			Mass bMass = event.getB().get(Mass.class);

			if (aCollidable && aVelocity != null) {
				if (aTransform != null && aMass != null && bCollidable && bVelocity != null && bTransform != null && bMass != null) {
					collide(aTransform.getPosition(), bTransform.getPosition(), aVelocity.getValue(), bVelocity.getValue(), aMass.getValue(), bMass.getValue());
				} else {
					reflect(aVelocity.getValue(), event.getMtvA());
				}
			} else {
				reflect(bVelocity.getValue(), event.getMtvB());
			}
		}
	}
	private void collide(Vector3 aPos, Vector3 bPos, Vector3 aVelocity, Vector3 bVelocity, double aMass, double bMass) {
		// save as will be mutated before 2nd use
		vTemp.set(aVelocity);

		applyCollide(aPos, bPos, aVelocity, bVelocity, aMass, bMass);
		applyCollide(bPos, aPos, bVelocity, vTemp, bMass, aMass);
	}
	private void applyCollide(Vector3 aPos, Vector3 bPos, Vector3 aVelocity, Vector3 bVelocity, double aMass, double bMass) {
		vDiff.set(aVelocity);
		vDiff.add(bVelocity, -1);

		sDiff.set(aPos);
		sDiff.add(bPos, -1);

		if (sDiff.getX() != 0 || sDiff.getY() != 0 || sDiff.getZ() != 0) {
			aVelocity.add(
					sDiff,
					-((2 * bMass) / (aMass + bMass))
							* Vector3.dot(vDiff, sDiff)
							/ Vector3.dot(sDiff, sDiff)
			);
		}
	}
	private static void reflect(Vector2 velocity, Vector2 normal) {
		velocity.reflect(normal);
	}

	@Override
	protected void after() {
		events.clear();
	}
}
