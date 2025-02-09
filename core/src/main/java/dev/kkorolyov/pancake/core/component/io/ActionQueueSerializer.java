package dev.kkorolyov.pancake.core.component.io;

import dev.kkorolyov.pancake.core.component.ActionQueue;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;

/**
 * Serializes {@link ActionQueue}s.
 */
public final class ActionQueueSerializer implements Serializer<ActionQueue> {
	@Override
	public void write(ActionQueue value, WriteContext context) {
		context.putInt(value.size());
		// note this skips buffered actions
		for (Action action : value) {
			context.putString(action.getClass().getName());
			context.putObject(action);
		}
	}
	@Override
	public ActionQueue read(ReadContext context) {
		var result = new ActionQueue();

		var size = context.getInt();
		for (int i = 0; i < size; i++) {
			try {
				result.add(context.getObject((Class<Action>) Class.forName(context.getString())));
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("cannot read serialized queued action", e);
			}
		}

		return result;
	}

	@Override
	public Class<ActionQueue> getType() {
		return ActionQueue.class;
	}
}
