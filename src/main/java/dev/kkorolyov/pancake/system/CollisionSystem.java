package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.component.collision.Bounds;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.core.Entity;
import dev.kkorolyov.pancake.core.GameSystem;
import dev.kkorolyov.pancake.core.Signature;

/**
 * Detects and handles entity collisions.
 */
public class CollisionSystem extends GameSystem {
	/**
	 * Constructs a new collision system.
	 */
	public CollisionSystem() {
		super(new Signature(Transform.class,
												Bounds.class));
	}
	
	@Override
	public void update(Entity entity, float dt) {
		// TODO
	}
}
