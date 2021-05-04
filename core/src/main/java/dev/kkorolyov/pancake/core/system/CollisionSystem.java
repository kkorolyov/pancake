package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Bounds;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.movement.Mass;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.core.event.EntitiesCollided;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.math.Collider;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.VectorMath;
import dev.kkorolyov.pancake.platform.utility.Limiter;

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
	// TODO Better collision detection alg (Current n^2)
	private final Set<Entity> done = new LinkedHashSet<>();
	private final Queue<Entity> moved = new ArrayDeque<>();

	/**
	 * Constructs a new collision system.
	 */
	public CollisionSystem() {
		super(
				new Signature(Transform.class, Bounds.class),
				Limiter.fromConfig(CollisionSystem.class)
		);
	}

	@Override
	public void update(Entity entity, long dt) {
		(isMoveable(entity) && VectorMath.magnitude(entity.get(Velocity.class).getValue()) > 0
				? moved
				: done
		).add(entity);
	}

	@Override
	public void after(long dt) {
		while (!moved.isEmpty()) {
			Entity movedEntity = moved.remove();

			for (Entity otherEntity : done) {
				Vector3 mtv = intersection(movedEntity, otherEntity);

				if (mtv != null) {
					movedEntity.get(Transform.class).getPosition().add(mtv);

					if (isMoveable(otherEntity)) {  // Entities in "moved" are already verified to be MOVEABLE
						Collider.elasticCollide(movedEntity.get(Transform.class).getPosition(), movedEntity.get(Velocity.class).getValue(), movedEntity.get(Mass.class).getValue(),
								otherEntity.get(Transform.class).getPosition(), otherEntity.get(Velocity.class).getValue(), otherEntity.get(Mass.class).getValue()
						);
					} else {
						mtv.scale(1 /VectorMath.magnitude(mtv));

						Vector3 velocity = movedEntity.get(Velocity.class).getValue();
						velocity.add(mtv, VectorMath.magnitude(velocity));
					}
					enqueue(new EntitiesCollided(movedEntity.getId(), otherEntity.getId()));
				}
			}
			done.add(movedEntity);
		}
		done.clear();
	}
	private Vector3 intersection(Entity e1, Entity e2) {  // TODO Optimize performance
		Transform t1 = e1.get(Transform.class);
		Transform t2 = e2.get(Transform.class);

		Bounds b1 = e1.get(Bounds.class);
		Bounds b2 = e2.get(Bounds.class);

		switch (b1.getIntersectionType(b2)) {
			case BOX_BOX:
				return Collider.intersection(t1.getPosition(), b1.getBox(),
						t2.getPosition(), b2.getBox()
				);
			case SPHERE_SPHERE:
				return Collider.intersection(t1.getPosition(), b1.getRadius(),
						t2.getPosition(), b2.getRadius()
				);
			case BOX_SPHERE:
				return Collider.intersection(t1.getPosition(), b1.getBox(),
						t2.getPosition(), b2.getRadius()
				);
			case SPHERE_BOX:
				return Collider.intersection(t2.getPosition(), b2.getBox(),
						t1.getPosition(), b1.getRadius()
				);
			default:
				return null;
		}
	}

	private boolean isMoveable(Entity entity) {
		return entity.get(Mass.class) != null
				&& entity.get(Velocity.class) != null;
	}
}
