package dev.kkorolyov.killstreek;

import dev.kkorolyov.pancake.core.component.Spawner;
import dev.kkorolyov.pancake.core.component.Sprite;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.platform.input.Action;

import java.util.Arrays;
import java.util.Collection;

import static dev.kkorolyov.killstreek.Constants.MOVE_FORCE;

public final class ActionsDirectory {
	public static final Collection<Action> actions = Arrays.asList(
			new Action("FORCE_UP", e -> e.get(Force.class).getForce().translate(0, MOVE_FORCE)),
			new Action("FORCE_DOWN", e -> e.get(Force.class).getForce().translate(0, -MOVE_FORCE)),
			new Action("FORCE_RIGHT", e -> e.get(Force.class).getForce().translate(MOVE_FORCE, 0)),
			new Action("FORCE_LEFT", e -> e.get(Force.class).getForce().translate(-MOVE_FORCE, 0)),
			new Action("RESET", e -> e.get(Transform.class).getPosition().set(0, 0)),
			new Action("WALK", e -> e.get(Sprite.class).stop(false, false)),
			new Action("TOGGLE_ANIMATION", e -> {
				Sprite sprite = e.get(Sprite.class);
				sprite.stop(!sprite.isStopped(), false);
			}),
			new Action("TOGGLE_SPAWNER", e -> {
				Spawner spawner = e.get(Spawner.class);
				spawner.setActive(!spawner.isActive());
			})
	);

	private ActionsDirectory() {}
}
