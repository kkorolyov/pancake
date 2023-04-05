package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.ActionQueue;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;

public final class ActionQueueComponentConverter implements ComponentConverter<ActionQueue> {
	@Override
	public ActionQueue read(Object data) {
		return new ActionQueue();
	}
	@Override
	public Object write(ActionQueue actions) {
		// TODO write actions?
		return new Object[0];
	}

	@Override
	public Class<ActionQueue> getType() {
		return ActionQueue.class;
	}
}
