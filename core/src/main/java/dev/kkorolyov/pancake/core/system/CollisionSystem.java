package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.movement.Mass;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.core.event.EntitiesIntersected;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;
import dev.kkorolyov.pancake.platform.utility.Limiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Collections.singleton;

/**
 * Responds to entity intersection events with additional elastic collisions where applicable.
 */
public class CollisionSystem extends GameSystem {
	private static final Logger LOG = LoggerFactory.getLogger(CollisionSystem.class);

	private final Collection<EntitiesIntersected> events = new ArrayList<>();
	private final Vector2 mtv = Vectors.create(0, 0);
	private final Vector3 vTemp = Vectors.create(0, 0, 0);
	private final Vector3 vDiff = Vectors.create(0, 0, 0);
	private final Vector3 sDiff = Vectors.create(0, 0, 0);

	/**
	 * Constructs a new collision system.
	 */
	public CollisionSystem() {
		super(
				// only responds to events
				singleton(null),
				Limiter.fromConfig(CollisionSystem.class)
		);
	}

	@Override
	public void attach() {
		register(EntitiesIntersected.class, events::add);
	}

	@Override
	public void update(Entity entity, long dt) {
		LOG.warn("CollisionSystem needless update call with {}", entity);
	}

	@Override
	public void after(long dt) {
		for (EntitiesIntersected event : events) {
			Transform aTransform = event.getA().get(Transform.class);
			Transform bTransform = event.getB().get(Transform.class);
			Velocity aVelocity = event.getA().get(Velocity.class);
			Velocity bVelocity = event.getB().get(Velocity.class);
			Mass aMass = event.getA().get(Mass.class);
			Mass bMass = event.getB().get(Mass.class);

			mtv.set(event.getMtv());
			if (aVelocity != null) {
				if (aMass != null && bVelocity != null && bMass != null) collide(aTransform.getPosition(), bTransform.getPosition(), aVelocity.getValue(), bVelocity.getValue(), aMass.getValue(), bMass.getValue());
				else if (!sameyDirection(aVelocity.getValue(), mtv)) {
					mtv.normalize();
					reflect(aVelocity.getValue(), mtv);
				}
			} else if (bVelocity != null) {
				if (sameyDirection(bVelocity.getValue(), mtv)) {
					// reverse so relative to B
					mtv.scale(-1);
					mtv.normalize();
					reflect(bVelocity.getValue(), mtv);
				}
			}
		}
		events.clear();
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

	private static boolean sameyDirection(Vector2 a, Vector2 b) {
		return (a.getX() == 0 || b.getX() == 0 || a.getX() > 0 == b.getX() > 0)
				&& (a.getY() == 0 || b.getY() == 0 || a.getY() > 0 == b.getY() > 0);
	}
}
