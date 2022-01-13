package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.ActionQueue;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.utility.Limiter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Dequeues and applies actions on entities.
 */
public class ActionSystem extends GameSystem {
	private final Collection<ActionQueue> actionQueues = new HashSet<>();

	/**
	 * Constructs a new action system.
	 */
	public ActionSystem() {
		super(
				List.of(ActionQueue.class),
				Limiter.fromConfig(ActionSystem.class)
		);
	}

	@Override
	public void update(Entity entity, long dt) {
		ActionQueue actionQueue = entity.get(ActionQueue.class);
		actionQueue.apply(entity);
		actionQueues.add(actionQueue);
	}

	@Override
	public void after(long dt) {
		for (ActionQueue actionQueue : actionQueues) {
			actionQueue.clear();
		}
		actionQueues.clear();
	}
}
