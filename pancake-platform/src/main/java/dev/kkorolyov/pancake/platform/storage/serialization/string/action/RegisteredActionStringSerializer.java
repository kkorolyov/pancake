package dev.kkorolyov.pancake.platform.storage.serialization.string.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;

/**
 * Serializes actions found in the {@link ActionRegistry}.
 */
public class RegisteredActionStringSerializer extends StringSerializer<Action> {
	private final ActionRegistry registry;

	/**
	 * Constructs a new registered action serializer.
	 * @param registry associated action registry
	 */
	public RegisteredActionStringSerializer(ActionRegistry registry) {
		super("[_a-zA-Z]+");
		this.registry = registry;
	}

	@Override
	public Action read(String out) {
		return registry.get(out);
	}
	@Override
	public String write(Action in) {
		return registry.getName(in);
	}
}
