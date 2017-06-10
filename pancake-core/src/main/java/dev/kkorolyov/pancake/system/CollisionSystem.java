package dev.kkorolyov.pancake.system;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.component.collision.BoxBounds;
import dev.kkorolyov.pancake.component.collision.SphereBounds;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.math.Collider;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Detects and handles entity collisions.
 */
public abstract class CollisionSystem extends GameSystem {
	// TODO Better collision detection alg (Current n^2)
	private final List<Entity> done = new ArrayList<>();
	private final Queue<Entity> moved = new ArrayDeque<>();

	/**
	 * Constructs a new collision system.
	 */
	public CollisionSystem(Class<? extends Component> bounds) {
		super(new Signature(Transform.class,
												bounds,
												Velocity.class,
												Force.class));
	}

	@Override
	public void update(Entity entity, float dt) {
		((entity.get(Velocity.class).getVelocity().getMagnitude() > 0) ? moved : done).add(entity);
	}

	@Override
	public void after(float dt) {
		while (!moved.isEmpty()) {
			Entity entity = moved.remove();

			for (Entity other : done) {
				Vector mtv = intersection(entity, other);

				if (mtv != null) {
					entity.get(Transform.class).getPosition().add(mtv);

					Collider.elasticCollide(entity.get(Transform.class).getPosition(), entity.get(Velocity.class).getVelocity(), entity.get(Force.class).getMass(),
																	other.get(Transform.class).getPosition(), other.get(Velocity.class).getVelocity(), other.get(Force.class).getMass());
				}
			}
			done.add(entity);
		}
		done.clear();
	}
	protected abstract Vector intersection(Entity e1, Entity e2);

	/**
	 * Detects and handles collisions between boxes.
	 */
	public static class BoxCollisionSystem extends CollisionSystem {
		/**
		 * Constructs a new box collision system.
		 */
		public BoxCollisionSystem() {
			super(BoxBounds.class);
		}

		@Override
		protected Vector intersection(Entity e1, Entity e2) {
			return Collider.intersection(e1.get(Transform.class).getPosition(), e1.get(BoxBounds.class).getSize(),
																	 e2.get(Transform.class).getPosition(), e2.get(BoxBounds.class).getSize());
		}
	}
	/**
	 * Detects and handles collisions between spheres.
	 */
	public static class SphereCollisionSystem extends CollisionSystem {
		/**
		 * Constructs a new sphere collision system.
		 */
		public SphereCollisionSystem() {
			super(SphereBounds.class);
		}

		@Override
		protected Vector intersection(Entity e1, Entity e2) {
			return Collider.intersection(e1.get(Transform.class).getPosition(), e1.get(SphereBounds.class).getRadius(),
																	 e2.get(Transform.class).getPosition(), e2.get(SphereBounds.class).getRadius());
		}
	}
}
