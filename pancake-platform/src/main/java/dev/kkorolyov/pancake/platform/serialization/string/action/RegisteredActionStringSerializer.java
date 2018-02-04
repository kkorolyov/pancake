package dev.kkorolyov.pancake.platform.serialization.string.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;

/**
 * Serializes actions found in the {@link ActionRegistry}.
 */
public class RegisteredActionStringSerializer extends ActionStringSerializer<Action> {
	/**
	 * Constructs a new registered action serializer.
	 * @param context associated action registry
	 */
	public RegisteredActionStringSerializer(ActionRegistry context) {
		super("[_a-zA-Z]+", context);
	}

	@Override
	public Action read(String out) {
		return context.get(out);
	}
	@Override
	public String write(Action in) {
		return context.getName(in);
	}
}
