package dev.kkorolyov.pancake.core.action;

import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * Applies a force.
 */
public class ForceAction extends Action {
	private final Vector force;

	/**
	 * Constructs a new force action.
	 * @param force force to add to accepted entities
	 */
	public ForceAction(Vector force) {
		super(Force.class);

		this.force = force;
	}

	@Override
	protected void apply(Entity entity) {
		entity.get(Force.class)
				.getForce().add(force);
	}
}
