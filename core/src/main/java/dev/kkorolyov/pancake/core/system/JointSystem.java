package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Joint;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;

import java.util.Map;

/**
 * Enforces joint connections between entities.
 */
public final class JointSystem extends GameSystem {
	public JointSystem() {
		super(Joint.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		for (Map.Entry<Entity, Joint.Constraint> entry : entity.get(Joint.class)) {
			entry.getValue().apply(entity, entry.getKey());
		}
	}
}
