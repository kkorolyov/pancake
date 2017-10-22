package dev.kkorolyov.pancake.platform.storage.serialization.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;

/**
 * Serializes actions found in the {@link ActionRegistry}.
 */
public class RegisteredActionSerializer extends ActionSerializer<Action> {
	/**
	 * Constructs a new registered action serializer.
	 */
	public RegisteredActionSerializer() {
		super("[_a-zA-Z]+");
	}

	@Override
	public Action read(String out, ActionRegistry context) {
		return context.get(out);
	}
	@Override
	public String write(Action in, ActionRegistry context) {
		return context.getName(in);
	}
}
