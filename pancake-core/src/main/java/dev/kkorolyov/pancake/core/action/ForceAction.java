package dev.kkorolyov.pancake.core.action;

import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.math.Vector;

import java.util.Objects;

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
	protected void apply(int id, EntityPool entities) {
		entities.get(id, Force.class)
				.getForce().add(force);
	}

	/** @return force vector added to accepted entities */
	public Vector getForce() {
		return force;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !getClass().isAssignableFrom(obj.getClass())) return false;

		ForceAction o = (ForceAction) obj;
		return Objects.equals(force, o.force);
	}
	@Override
	public int hashCode() {
		return Objects.hash(force);
	}
}
