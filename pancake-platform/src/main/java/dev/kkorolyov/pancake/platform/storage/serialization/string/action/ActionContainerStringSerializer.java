package dev.kkorolyov.pancake.platform.storage.serialization.string.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.string.action.ActionContainerStringSerializer.ActionContainer;

/**
 * Serializes actions associated with some identifier.
 */
public class ActionContainerStringSerializer extends StringSerializer<ActionContainer> {
	private final ActionRegistry registry;

	/**
	 * Constructs a new action container serializer.
	 * @param registry associated action registry
	 */
	public ActionContainerStringSerializer(ActionRegistry registry) {
		super("[_a-zA-Z]+\\s*=\\s*.+");
		this.registry = registry;
	}

	@Override
	public ActionContainer read(String out) {
		String[] split = out.split("\\s?=\\s?");
		String name = split[0], actionS = split[1];

		return new ActionContainer(name, registry.readAction(actionS));
	}
	@Override
	public String write(ActionContainer in) {
		// TODO
		return null;
	}

	/**
	 * Contains a single name-action association.
	 */
	public class ActionContainer {
		public final String name;
		public final Action action;

		private ActionContainer(String name, Action action) {
			this.name = name;
			this.action = action;
		}
	}
}
