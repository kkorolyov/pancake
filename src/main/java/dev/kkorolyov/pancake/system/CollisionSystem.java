package dev.kkorolyov.pancake.system;

import java.util.Map.Entry;

import dev.kkorolyov.pancake.engine.Component;
import dev.kkorolyov.pancake.engine.EntityManager;
import dev.kkorolyov.pancake.engine.GameSystem;
import dev.kkorolyov.pancake.component.Bounds;
import dev.kkorolyov.pancake.component.Transform;

/**
 * Detects and handles entity collisions.
 */
public class CollisionSystem extends GameSystem {
	/**
	 * Constructs a new collision system.
	 */
	public CollisionSystem() {
		super(Transform.class, Bounds.class);
	}
	
	@Override
	public int update(float dt) {
		int counter = 0;
		EntityManager entities = getEntities();
		for (Entry<Integer, Component> entity : entities.getAll(Bounds.class)) {
			Bounds bounds = (Bounds) entity.getValue();
			Transform transform = entities.get(entity.getKey(), Transform.class);
			
			if (bounds != null & transform != null)
		}
		return counter;
	}
}
