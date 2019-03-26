package dev.kkorolyov.pancake.platform.serialization.string.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;

/**
 * Serializes actions found in the {@link ActionRegistry}.
 */
public class ActionReferenceStringSerializer extends ActionStringSerializer<Action> {
	/**
	 * Constructs a new registered action serializer.
	 * @param context associated action registry
	 */
	public ActionReferenceStringSerializer(ActionRegistry context) {
		super("\\w+", context);
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
