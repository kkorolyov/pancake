package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.event.Intersecting;
import dev.kkorolyov.pancake.core.component.movement.Mass;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;

/**
 * Updates intersecting entities with additional elastic collisions where applicable.
 */
public final class CollisionSystem extends GameSystem {
	private final Vector2 mtv = Vectors.create(0, 0);
	private final Vector3 vTemp = Vectors.create(0, 0, 0);
	private final Vector3 vDiff = Vectors.create(0, 0, 0);
	private final Vector3 sDiff = Vectors.create(0, 0, 0);

	/**
	 * Constructs a new collision system.
	 */
	public CollisionSystem() {
		super(Intersecting.class);
	}

	@Override
	public void update(Entity entity, long dt) {
		Intersecting intersecting = entity.get(Intersecting.class);

		Transform aTransform = entity.get(Transform.class);
		Transform bTransform = intersecting.getOther().get(Transform.class);
		Velocity aVelocity = entity.get(Velocity.class);
		Velocity bVelocity = intersecting.getOther().get(Velocity.class);
		Mass aMass = entity.get(Mass.class);
		Mass bMass = intersecting.getOther().get(Mass.class);

		mtv.set(intersecting.getMtv());
		if (aVelocity != null) {
			if (aMass != null && bVelocity != null && bMass != null) collide(aTransform.getPosition(), bTransform.getPosition(), aVelocity.getValue(), bVelocity.getValue(), aMass.getValue(), bMass.getValue());
			else {
				reflect(aVelocity.getValue(), mtv);
			}
		} else if (bVelocity != null) {
			// reverse so relative to B
			mtv.scale(-1);
			reflect(bVelocity.getValue(), mtv);
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
}
