package dev.kkorolyov.pancake.system;

import java.util.Map.Entry;

import dev.kkorolyov.pancake.engine.Component;
import dev.kkorolyov.pancake.engine.EntityManager;
import dev.kkorolyov.pancake.engine.GameSystem;
import dev.kkorolyov.pancake.component.*;

/**
 * Updates entities using physics calculations.
 */
public class PhysicsSystem extends GameSystem {
	/**
	 * Constructs a new physics system.
	 */
	public PhysicsSystem() {
		super(Velocity.class);
	}
	@Override
	public int update(float dt) {
		int counter = 0;
		EntityManager entities = getEntities();
		for (Entry<Integer, Component> entity : entities.getAll(Velocity.class)) {
			Velocity velocity = (Velocity) entity.getValue();
			
			if (velocity != null) {
				Force force = entities.get(entity.getKey(), Force.class);
				if (force != null) {
					Damping damping = entities.get(entity.getKey(), Damping.class);	// TODO Remove force dependency
					if (damping != null)
						damping.damp(velocity.getVelocity(), force.getForce());
					
					force.accelerate(velocity.getVelocity(), dt);
				}
				MaxSpeed maxSpeed = entities.get(entity.getKey(), MaxSpeed.class);
				if (maxSpeed != null)
					maxSpeed.cap(velocity.getVelocity());
				
				velocity.round();
				
				Transform transform = entities.get(entity.getKey(), Transform.class);
				if (transform != null)
					velocity.move(transform.getPosition(), dt);
				
				counter++;
			}
		}
		return counter;
	}
}
