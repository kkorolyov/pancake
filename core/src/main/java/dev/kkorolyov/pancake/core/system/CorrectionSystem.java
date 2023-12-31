package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.event.Intersected;
import dev.kkorolyov.pancake.core.component.tag.Correctable;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Collection;
import java.util.HashSet;

/**
 * Repositions {@link Correctable} {@link Intersected} entities just enough to remove the immediate intersection.
 * <p>
 * In any intersection where both entities are {@link Correctable}, repositions that entity with the lesser {@link Correctable} component.
 * If both {@link Correctable} components are equal, repositions both entities by half the repositioning distance.
 */
public final class CorrectionSystem extends GameSystem {
	private final Collection<Intersected.Event> events = new HashSet<>();

	public CorrectionSystem() {
		super(Intersected.class, Correctable.class, Transform.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		for (Intersected.Event event : entity.get(Intersected.class)) {
			if (events.add(event) && event.getA().get(Correctable.class) != null && event.getB().get(Correctable.class) != null) {
				int priority = event.getA().get(Correctable.class).compareTo(event.getB().get(Correctable.class));

				Vector3 aPosition = event.getA().get(Transform.class).getTranslation();
				Vector3 bPosition = event.getB().get(Transform.class).getTranslation();

				if (priority == 0) {
					// split the correction
					aPosition.add(event.getMtvA(), 0.5);
					bPosition.add(event.getMtvB(), 0.5);
				} else if (priority < 0) {
					aPosition.add(event.getMtvA());
				} else {
					bPosition.add(event.getMtvB());
				}
			}
		}
	}

	@Override
	protected void after(long dt) {
		events.clear();
	}
}
