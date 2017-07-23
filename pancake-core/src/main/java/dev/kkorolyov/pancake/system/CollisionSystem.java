package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.component.collision.Bounds;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.math.Collider;
import dev.kkorolyov.pancake.math.Vector;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;

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
		Velocity velocity = entity.get(Velocity.class);
		((velocity != null && velocity.getVelocity().getMagnitude() > 0) ? moved : done).add(entity);
	}

	@Override
	public void after(float dt) {
		while (!moved.isEmpty()) {
			Entity entity = moved.remove();

			for (Entity other : done) {
				Vector mtv = intersection(entity, other);

				if (mtv != null) {
					entity.get(Transform.class).getPosition().add(mtv);

					if (entity.contains(MOVEABLE)) {
						if (other.contains(MOVEABLE)) {
							Collider.elasticCollide(entity.get(Transform.class).getPosition(), entity.get(Velocity.class).getVelocity(), entity.get(Force.class).getMass(),
									other.get(Transform.class).getPosition(), other.get(Velocity.class).getVelocity(), other.get(Force.class).getMass());
						} else {
							mtv.normalize();

							Vector velocity = entity.get(Velocity.class).getVelocity();
							velocity.add(mtv, velocity.getMagnitude());
						}
					}
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

		return (b1.getBox() != null)
				? (b2.getBox() != null)
					? Collider.intersection(t1.getPosition(), b1.getBox(),	// Box-Box
						t2.getPosition(), b2.getBox())
					: Collider.intersection(t1.getPosition(), b1.getBox(),	// Box-Sphere
						t2.getPosition(), b2.getRadius())
				: (b2.getRadius() != null)
					? Collider.intersection(t1.getPosition(), b1.getRadius(),	// Sphere-Sphere
						t2.getPosition(), b2.getRadius())
					: Collider.intersection(t2.getPosition(), b2.getBox(),	// Sphere-Box
						t1.getPosition(), b1.getRadius());
	}
}
