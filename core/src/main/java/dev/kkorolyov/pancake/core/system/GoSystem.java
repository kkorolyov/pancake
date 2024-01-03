package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Force;
import dev.kkorolyov.pancake.core.component.Go;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Makes entities go where they request to, relative to their parent transform.
 * Alters {@link Force} with {@link Go#getStrength()} magnitude to get {@link Transform} translation within {@link Go#getProximity()} units of {@link Go#getTarget()}.
 * Removes {@link Go} components from entities that have reached their targets.
 */
public final class GoSystem extends GameSystem {
	private static final ThreadLocal<Vector3> tNewForce = ThreadLocal.withInitial(Vector3::of);

	public GoSystem() {
		super(Go.class, Transform.class, Force.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		Go go = entity.get(Go.class);
		Transform transform = entity.get(Transform.class);
		Force force = entity.get(Force.class);

		Vector3 newForce = tNewForce.get();
		newForce.set(go.getTarget());
		newForce.add(transform.getTranslation(), -1);

		double magnitude = Vector3.magnitude(newForce);
		if (magnitude > go.getProximity()) {
			newForce.scale(go.getStrength() / magnitude);
			// TODO add to force, rather than set it?
			force.getValue().set(newForce);
		} else {
			// done moving - reset force, remove the Go, maybe snap position
			force.getValue().scale(0);
			entity.remove(Go.class);

			if (go.isSnap()) {
				transform.getTranslation().set(go.getTarget());
				// FIXME weird mucking with Velocity here
				var velocity = entity.get(Velocity.class);
				if (velocity != null) velocity.getValue().scale(0);
			}
		}
	}
}
