package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.core.component.event.Intersected;
import dev.kkorolyov.pancake.core.component.tag.Correctable;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Repositions {@link Correctable} {@link Intersected} entities just enough to remove the immediate intersection.
 * <p>
 * In any intersection, repositions that entity with the lesser non-{@code null} {@link Correctable} component.
 * If both {@link Correctable} components are equal, repositions both entities by half the repositioning distance.
 */
public final class CorrectionSystem extends GameSystem {
	private static final Comparator<Correctable> COMPARATOR = Comparator.nullsLast(Comparator.naturalOrder());

	private final Collection<Intersected> events = new HashSet<>();

	public CorrectionSystem() {
		super(Intersected.class, Correctable.class, Position.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		Intersected event = entity.get(Intersected.class);
		if (events.add(event)) {
			int priority = COMPARATOR.compare(event.getA().get(Correctable.class), event.getB().get(Correctable.class));

			Position aPosition = event.getA().get(Position.class);
			Position bPosition = event.getB().get(Position.class);

			if (priority <= 0 && aPosition != null) {
				if (priority == 0 && bPosition != null) {
					// split the correction
					aPosition.getValue().add(event.getMtvA(), 0.5);
					bPosition.getValue().add(event.getMtvB(), 0.5);
				} else {
					aPosition.getValue().add(event.getMtvA());
				}
			} else {
				bPosition.getValue().add(event.getMtvB());
			}
		}
	}

	@Override
	protected void after() {
		events.clear();
	}
}
