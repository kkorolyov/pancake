package dev.kkorolyov.pancake.platform.serialization.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
import dev.kkorolyov.pancake.platform.serialization.Serializer;
import dev.kkorolyov.pancake.platform.serialization.action.ActionContainerSerializer.ActionContainer;
import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;

import java.util.Objects;

/**
 * Serializes actions associated with some identifier.
 */
public class ActionContainerSerializer extends StringSerializer<ActionContainer> {
	private final ActionRegistry context;
	private final Serializer<Action, String> autoSerializer;

	/**
	 * Constructs a new action container serializer.
	 * @param context associated action registry
	 */
	public ActionContainerSerializer(ActionRegistry context) {
		super("[_a-zA-Z]+\\s*=\\s*.+");
		this.context = context;
		autoSerializer = new AutoSerializer<>(ActionSerializer.class, context);
	}

	@Override
	public ActionContainer read(String out) {
		String[] split = out.split("\\s?=\\s?");
		String name = split[0], actionS = split[1];

		return new ActionContainer(name, autoSerializer.read(actionS));
	}
	@Override
	public String write(ActionContainer in) {
		return in.name + "=" + autoSerializer.write(in.action);
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

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || !getClass().isAssignableFrom(obj.getClass())) return false;

			ActionContainer o = (ActionContainer) obj;
			return Objects.equals(name, o.name)
					&& Objects.equals(action, o.action);
		}
		@Override
		public int hashCode() {
			return Objects.hash(name, action);
		}
	}
}
