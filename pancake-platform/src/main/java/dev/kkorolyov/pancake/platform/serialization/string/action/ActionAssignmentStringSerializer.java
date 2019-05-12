package dev.kkorolyov.pancake.platform.serialization.string.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
import dev.kkorolyov.pancake.platform.serialization.Serializer;
import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;

/**
 * Serializes actions assigned to some identifier.
 * Reads add the deserialized action to the associated {@link ActionRegistry}.
 */
public class ActionAssignmentStringSerializer extends StringSerializer<Action> {
	private final ActionRegistry context;
	private final Serializer<Action, String> autoSerializer;

	public ActionAssignmentStringSerializer() {
		this(null);
	}
	/**
	 * Constructs a new action container serializer.
	 * @param context associated action registry
	 */
	public ActionAssignmentStringSerializer(ActionRegistry context) {
		super(new ActionReferenceStringSerializer(context).pattern() + "\\s*=.+");
		this.context = context;
		autoSerializer = new AutoSerializer<>(ActionStringSerializer.class, context);
	}

	@Override
	public Action read(String out) {
		String[] split = out.split("\\s*=\\s*");
		Action action = autoSerializer.read(split[1]);

		context.put(split[0], action);
		return action;
	}
	@Override
	public String write(Action in) {
		return context.getName(in) + "=" + autoSerializer.write(in);
	}
}
