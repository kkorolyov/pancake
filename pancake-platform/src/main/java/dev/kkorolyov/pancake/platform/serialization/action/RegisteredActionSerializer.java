package dev.kkorolyov.pancake.platform.serialization.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;

/**
 * Serializes actions found in the {@link ActionRegistry}.
 */
public class RegisteredActionSerializer extends ActionSerializer<Action> {
	/**
	 * Constructs a new registered action serializer.
	 * @param context associated action registry
	 */
	public RegisteredActionSerializer(ActionRegistry context) {
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
