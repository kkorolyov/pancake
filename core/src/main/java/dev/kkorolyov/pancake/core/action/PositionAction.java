package dev.kkorolyov.pancake.core.action;

import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Objects;

/**
 * Sets a position.
 */
public class PositionAction implements Action {
	private final Vector3 position;

	/**
	 * Constructs a new position action.
	 * @param position position to set on accepted entities
	 */
	public PositionAction(Vector3 position) {
		this.position = Vector3.of(position);
	}

	@Override
	public void apply(Entity entity) {
		entity.get(Position.class).getValue().set(position);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !getClass().isAssignableFrom(obj.getClass())) return false;

		PositionAction o = (PositionAction) obj;
		return Objects.equals(position, o.position);
	}
	@Override
	public int hashCode() {
		return Objects.hash(position);
	}

	@Override
	public String toString() {
		return "PositionAction{" +
				"position=" + position +
				'}';
	}
}
