package dev.kkorolyov.pancake.core.action;

import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;

import java.util.Objects;

/**
 * Adds an amount to an entity's existing force component.
 */
public final class ForceAction implements Action {
	private final Vector3 force;

	/**
	 * Constructs a new force action.
	 * @param force force to add to accepted entities
	 */
	public ForceAction(Vector3 force) {
		this.force = Vectors.create(force);
	}

	@Override
	public void apply(Entity entity) {
		entity.get(Force.class).getValue().add(force);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		ForceAction o = (ForceAction) obj;
		return Objects.equals(force, o.force);
	}
	@Override
	public int hashCode() {
		return Objects.hash(force);
	}

	@Override
	public String toString() {
		return "ForceAction{" +
				"force=" + force +
				'}';
	}
}
