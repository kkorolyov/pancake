package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.ActionQueue;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Entity;

import java.util.Collection;
import java.util.HashSet;

/**
 * Dequeues and applies actions on entities.
 */
public final class ActionSystem extends GameSystem {
	private final Collection<ActionQueue> actionQueues = new HashSet<>();

	/**
	 * Constructs a new action system.
	 */
	public ActionSystem() {
		super(ActionQueue.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		ActionQueue actionQueue = entity.get(ActionQueue.class);
		for (Action action : actionQueue) action.apply(entity);
		actionQueues.add(actionQueue);
	}

	@Override
	public void after() {
		for (ActionQueue actionQueue : actionQueues) {
			actionQueue.clear();
		}
		actionQueues.clear();
	}
}
