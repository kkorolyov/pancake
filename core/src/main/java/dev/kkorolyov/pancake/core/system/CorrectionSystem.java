package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.event.Intersected;
import dev.kkorolyov.pancake.core.component.tag.Correctable;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;

import java.util.Collection;
import java.util.HashSet;

/**
 * Repositions {@link Correctable} {@link Intersected} entities just enough to remove the immediate intersection.
 */
public final class CorrectionSystem extends GameSystem {
	private final Collection<Intersected> events = new HashSet<>();

	public CorrectionSystem() {
		super(Intersected.class, Correctable.class, Transform.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		Intersected event = entity.get(Intersected.class);
		if (events.add(event)) {
			boolean aCorrectable = event.getA().get(Correctable.class) != null;
			boolean bCorrectable = event.getB().get(Correctable.class) != null;

			Transform aTransform = event.getA().get(Transform.class);
			Transform bTransform = event.getB().get(Transform.class);

			if (aCorrectable && aTransform != null) {
				if (bCorrectable && bTransform != null) {
					// split the correction
					aTransform.getPosition().add(event.getMtvA(), 0.5);
					bTransform.getPosition().add(event.getMtvB(), 0.5);
				} else {
					aTransform.getPosition().add(event.getMtvA());
				}
			} else {
				bTransform.getPosition().add(event.getMtvB());
			}
		}
	}

	@Override
	protected void after() {
		events.clear();
	}
}
