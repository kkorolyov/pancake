package dev.kkorolyov.pancake.platform.storage.serialization.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.storage.serialization.AutoContextualSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.ContextualSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.action.ActionContainerStringSerializer.ActionContainer;

/**
 * Serializes actions associated with some identifier.
 */
public class ActionContainerStringSerializer extends StringSerializer<ActionContainer> implements ContextualSerializer<ActionContainer, String, ActionRegistry> {
	private static final ContextualSerializer<Action, String, ActionRegistry> autoSerializer = new AutoContextualSerializer(ActionSerializer.class);

	/**
	 * Constructs a new action container serializer.
	 */
	public ActionContainerStringSerializer() {
		super("[_a-zA-Z]+\\s*=\\s*.+");
	}

	@Override
	public ActionContainer read(String out) {
		return read(out, new ActionRegistry());
	}
	@Override
	public ActionContainer read(String out, ActionRegistry context) {
		String[] split = out.split("\\s?=\\s?");
		String name = split[0], actionS = split[1];

		return new ActionContainer(name, autoSerializer.read(actionS, context));
	}

	@Override
	public String write(ActionContainer in) {
		return write(in, new ActionRegistry());
	}
	@Override
	public String write(ActionContainer in, ActionRegistry context) {
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
