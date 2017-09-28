package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Bounds;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.core.event.Events;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
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
	private final Set<Entity> done = new LinkedHashSet<>();
	private final Queue<Entity> moved = new ArrayDeque<>();

	/**
	 * Constructs a new collision system.
	 */
	public CollisionSystem() {
		super(new Signature(Transform.class,
				Bounds.class));
	}

	@Override
	public void update(Entity entity, float dt) {
		((entity.contains(MOVEABLE) && entity.get(Velocity.class).getVelocity().getMagnitude() > 0)
				? moved
				: done).add(entity);
	}

	@Override
	public void after(float dt) {
		while (!moved.isEmpty()) {
			Entity entity = moved.remove();

			for (Entity other : done) {
				Vector mtv = intersection(entity, other);

				if (mtv != null) {
					entity.get(Transform.class).getPosition().add(mtv);

					if (other.contains(MOVEABLE)) {	// Entities in "moved" are already verified to be MOVEABLE
						Collider.elasticCollide(entity.get(Transform.class).getPosition(), entity.get(Velocity.class).getVelocity(), entity.get(Force.class).getMass(),
								other.get(Transform.class).getPosition(), other.get(Velocity.class).getVelocity(), other.get(Force.class).getMass());
					} else {
						mtv.normalize();

						Vector velocity = entity.get(Velocity.class).getVelocity();
						velocity.add(mtv, velocity.getMagnitude());
					}
					enqueue(Events.COLLIDED, new Object[]{entity, other});	// TODO New Object[] may be a performance detriment
				}
			}
			done.add(entity);
		}
		done.clear();
	}
	private Vector intersection(Entity e1, Entity e2) {	// TODO Optimize performance
		Transform t1 = e1.get(Transform.class);
		Transform t2 = e2.get(Transform.class);

		Bounds b1 = e1.get(Bounds.class);
		Bounds b2 = e2.get(Bounds.class);

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
