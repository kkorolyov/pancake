package dev.kkorolyov.pancake.platform.action.io;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.io.RegisteredSerializer;

/**
 * Serializes {@link Action}s through registry references.
 */
public class ActionSerializer extends RegisteredSerializer<Action> {
	@Override
	public Class<Action> getType() {
		return Action.class;
	}
}
