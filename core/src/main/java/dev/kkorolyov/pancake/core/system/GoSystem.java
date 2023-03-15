package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Go;
import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Makes entities go where they request to.
 * Alters {@link Force} with {@link Go#getStrength()} magnitude to get {@link Position} within {@link Go#getBuffer()} units of {@link Go#getTarget()}.
 */
public final class GoSystem extends GameSystem {
	private static final ThreadLocal<Vector3> tNewForce = ThreadLocal.withInitial(Vector3::of);

	public GoSystem() {
		super(Go.class, Position.class, Force.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		Go go = entity.get(Go.class);
		Position position = entity.get(Position.class);
		Force force = entity.get(Force.class);

		Vector3 newForce = tNewForce.get();
		newForce.set(go.getTarget());
		newForce.add(position.getValue(), -1);

		double magnitude = Vector3.magnitude(newForce);
		if (magnitude > go.getBuffer()) {
			newForce.scale(go.getStrength() / magnitude);
			force.getValue().set(newForce);
		}
	}
}
