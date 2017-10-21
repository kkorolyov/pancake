package dev.kkorolyov.pancake.platform.storage.serialization.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.storage.serialization.ContextualSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;

/**
 * Serializes actions to strings.
 * @param <I> raw action instance type
 */
public abstract class ActionSerializer<I extends Action> extends StringSerializer<I> implements ContextualSerializer<I, String, ActionRegistry> {
	/**
	 * Constructs a new string serializer.
	 * @param pattern accepted regex pattern
	 */
	public ActionSerializer(String pattern) {
		super(pattern);
	}

	@Override
	public I read(String out) {
		return read(out, new ActionRegistry());
	}
	@Override
	public String write(I in) {
		return write(in, new ActionRegistry());
	}
}
