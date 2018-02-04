package dev.kkorolyov.pancake.platform.serialization.string.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;

/**
 * Serializes actions to strings.
 * @param <I> raw action instance type
 */
public abstract class ActionStringSerializer<I extends Action> extends StringSerializer<I> {
	protected final ActionRegistry context;

	/**
	 * Constructs a new action serializer.
	 * @param pattern accepted regex pattern
	 * @param context associated action registry
	 */
	protected ActionStringSerializer(String pattern, ActionRegistry context) {
		super(pattern);
		this.context = context;
	}
}
