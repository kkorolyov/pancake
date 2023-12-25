package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Mass;
import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.core.component.event.Intersected;
import dev.kkorolyov.pancake.core.component.tag.Collidable;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Collection;
import java.util.HashSet;

/**
 * Simmulates collisions for {@link Collidable} {@link Intersected} entities.
 * <p>
 * In any intersection where both entities are {@link Collidable}, reflects that entity with the lesser {@link Collidable} component.
 * If both {@link Collidable} components are equal, collides both entities.
 */
public final class CollisionSystem extends GameSystem {
	private final Vector3 vTemp = Vector3.of(0, 0, 0);
	private final Vector3 vDiff = Vector3.of(0, 0, 0);
	private final Vector3 sDiff = Vector3.of(0, 0, 0);

	private final Collection<Intersected.Event> events = new HashSet<>();

	/**
	 * Constructs a new collision system.
	 */
	public CollisionSystem() {
		super(Intersected.class, Collidable.class, Position.class, Velocity.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		for (Intersected.Event event : entity.get(Intersected.class)) {
			if (events.add(event) && event.getA().get(Collidable.class) != null && event.getB().get(Collidable.class) != null) {
				int priority = event.getA().get(Collidable.class).compareTo(event.getB().get(Collidable.class));

				Position aPosition = event.getA().get(Position.class);
				Position bPosition = event.getB().get(Position.class);

				Velocity aVelocity = event.getA().get(Velocity.class);
				Velocity bVelocity = event.getB().get(Velocity.class);

				Mass aMass = event.getA().get(Mass.class);
				Mass bMass = event.getB().get(Mass.class);

				if (priority <= 0 && aVelocity != null) {
					if (aMass != null && priority == 0 && bVelocity != null && bMass != null) {
						collide(aPosition.getValue(), bPosition.getValue(), aVelocity.getValue(), bVelocity.getValue(), aMass.getValue(), bMass.getValue());
					} else {
						reflect(aVelocity.getValue(), event.getMtvA());
					}
				} else {
					reflect(bVelocity.getValue(), event.getMtvB());
				}
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
	protected void after(long dt) {
		events.clear();
	}
}
