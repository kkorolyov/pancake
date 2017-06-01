package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.component.control.InteractiveEntityController;
import dev.kkorolyov.pancake.Entity;
import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.Signature;

/**
 * Applies actions to entities.
 */
public class ActionSystem extends GameSystem {
	/**
	 * Constructs a new action system.
	 */
	public ActionSystem() {
		super(new Signature(InteractiveEntityController.class));
	}

	@Override
	public void update(Entity entity, float dt) {
		entity.get(InteractiveEntityController.class).update(entity, dt);	// TODO Icky
	}
}
