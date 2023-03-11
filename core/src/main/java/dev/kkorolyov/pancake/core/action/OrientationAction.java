package dev.kkorolyov.pancake.core.action;

import dev.kkorolyov.pancake.core.component.Orientation;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Objects;

/**
 * Sets an orientation.
 */
public class OrientationAction implements Action {
	private final Vector3 orientation;

	/**
	 * Constructs a new orientation action.
	 * @param orientation orientation to set on accepted entities
	 */
	public OrientationAction(Vector3 orientation) {
		this.orientation = Vector3.of(orientation);
	}

	@Override
	public void apply(Entity entity) {
		entity.get(Orientation.class).getValue().set(orientation);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !getClass().isAssignableFrom(obj.getClass())) return false;

		OrientationAction o = (OrientationAction) obj;
		return Objects.equals(orientation, o.orientation);
	}
	@Override
	public int hashCode() {
		return Objects.hash(orientation);
	}

	@Override
	public String toString() {
		return "OrientationAction{" +
				"orientation=" + orientation +
				'}';
	}
}
