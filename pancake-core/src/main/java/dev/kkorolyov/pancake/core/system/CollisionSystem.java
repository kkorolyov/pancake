package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Bounds;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.core.event.EntitiesCollided;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.math.Collider;
import dev.kkorolyov.pancake.platform.math.Vector;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;

import static dev.kkorolyov.pancake.core.component.Bounds.BOX_BOX;
import static dev.kkorolyov.pancake.core.component.Bounds.BOX_SPHERE;
import static dev.kkorolyov.pancake.core.component.Bounds.SPHERE_BOX;
import static dev.kkorolyov.pancake.core.component.Bounds.SPHERE_SPHERE;

/**
 * Detects and handles entity collisions.
 */
public class CollisionSystem extends GameSystem {
	private static final Signature MOVEABLE = new Signature(Velocity.class, Force.class);

	// TODO Better collision detection alg (Current n^2)
	private final Set<Integer> done = new LinkedHashSet<>();
	private final Queue<Integer> moved = new ArrayDeque<>();

	/**
	 * Constructs a new collision system.
	 */
	public CollisionSystem() {
		super(new Signature(Transform.class,
				Bounds.class));
	}

	@Override
	public void update(int id, float dt) {
		((entities.contains(id, MOVEABLE) && entities.get(id, Velocity.class).getVelocity().getMagnitude() > 0)
				? moved
				: done).add(id);
	}

	@Override
	public void after(float dt) {
		while (!moved.isEmpty()) {
			int movedId = moved.remove();

			for (int otherId : done) {
				Vector mtv = intersection(movedId, otherId);

				if (mtv != null) {
					entities.get(movedId, Transform.class).getPosition().add(mtv);

					if (entities.contains(otherId, MOVEABLE)) {	// Entities in "moved" are already verified to be MOVEABLE
						Collider.elasticCollide(entities.get(movedId, Transform.class).getPosition(), entities.get(movedId, Velocity.class).getVelocity(), entities.get(movedId, Force.class).getMass(),
								entities.get(otherId, Transform.class).getPosition(), entities.get(otherId, Velocity.class).getVelocity(), entities.get(otherId, Force.class).getMass());
					} else {
						mtv.normalize();

						Vector velocity = entities.get(movedId, Velocity.class).getVelocity();
						velocity.add(mtv, velocity.getMagnitude());
					}
					events.enqueue(new EntitiesCollided(movedId, otherId));
				}
			}
			done.add(movedId);
		}
		done.clear();
	}
	private Vector intersection(int e1, int e2) {	// TODO Optimize performance
		Transform t1 = entities.get(e1, Transform.class);
		Transform t2 = entities.get(e2, Transform.class);

		Bounds b1 = entities.get(e1, Bounds.class);
		Bounds b2 = entities.get(e2, Bounds.class);

		switch (b1.getIntersectionType(b2)) {
			case BOX_BOX:
				return Collider.intersection(t1.getPosition(), b1.getBox(),
						t2.getPosition(), b2.getBox());
			case SPHERE_SPHERE:
				return Collider.intersection(t1.getPosition(), b1.getRadius(),
						t2.getPosition(), b2.getRadius());
			case BOX_SPHERE:
				return Collider.intersection(t1.getPosition(), b1.getBox(),
						t2.getPosition(), b2.getRadius());
			case SPHERE_BOX:
				return Collider.intersection(t2.getPosition(), b2.getBox(),
						t1.getPosition(), b1.getRadius());
			default:
				return null;
		}
	}
}
