package dev.kkorolyov.pancake.core.action;

import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;

import java.util.Objects;

/**
 * Sets entity velocity.
 */
public final class VelocityAction implements Action {
	private final Vector3 velocity;

	/**
	 * Constructs a new velocity action.
	 * @param velocity velocity to set
	 */
	public VelocityAction(Vector3 velocity) {
		this.velocity = Vectors.create(velocity);
	}

	@Override
	public void apply(Entity entity) {
		entity.get(Velocity.class).getValue().set(velocity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		VelocityAction o = (VelocityAction) obj;
		return Objects.equals(velocity, o.velocity);
	}
	@Override
	public int hashCode() {
		return Objects.hash(velocity);
	}

	@Override
	public String toString() {
		return "VelocityAction{" +
				"velocity=" + velocity +
				'}';
	}
}
