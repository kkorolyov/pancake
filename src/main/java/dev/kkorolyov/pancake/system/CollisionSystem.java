package dev.kkorolyov.pancake.system;

import java.util.ArrayList;
import java.util.List;

import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.component.collision.EllipseBounds;
import dev.kkorolyov.pancake.component.collision.RectangleBounds;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.core.Entity;
import dev.kkorolyov.pancake.core.GameSystem;
import dev.kkorolyov.pancake.core.Signature;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Detects and handles entity collisions.
 */
public class CollisionSystem extends GameSystem {
	// TODO Better collision detection alg (Current n^2)
	private final List<Entity> entities = new ArrayList<>();
	private final Vector vTemp = new Vector();

	/**
	 * Constructs a new collision system.
	 */
	public CollisionSystem() {
		super(new Signature(Transform.class,
												RectangleBounds.class,
												EllipseBounds.class,
												Force.class,
												Velocity.class));
	}
	
	@Override
	public void update(Entity entity, float dt) {
		entities.add(entity);
	}

	@Override
	public void after(float dt) {
		for (int i = 0; i < entities.size(); i++) {
			for (int j = 1; j < entities.size(); j++) {
				Entity e1 = entities.get(i);
				Entity e2 = entities.get(j);

				Transform t1 = e1.get(Transform.class);
				Transform t2 = e2.get(Transform.class);
				RectangleBounds b1 = e1.get(RectangleBounds.class);
				RectangleBounds b2 = e2.get(RectangleBounds.class);

				if (b1.intersects(b2, t1.getPosition(), t2.getPosition())) {
					collide(e1, e2);
				}
			}
		}

		entities.clear();
	}
	private void collide(Entity e1, Entity e2) {
		Vector v1 = e1.get(Velocity.class).getVelocity();
		Vector v2 = e2.get(Velocity.class).getVelocity();
		float m1 = e1.get(Force.class).getMass();
		float m2 = e2.get(Force.class).getMass();
		vTemp.set(e1.get(Velocity.class).getVelocity());

		applyCollision(v1, v2, m1, m2);
		applyCollision(v2, vTemp, m2, m1);
	}
	private void applyCollision(Vector v1, Vector v2, float m1, float m2) {
		v1.set(v2);

		v1.scale(m1 - m2);
		v1.add(v2, m2);
		v1.scale(1 / (m1 + m2));
	}
}
