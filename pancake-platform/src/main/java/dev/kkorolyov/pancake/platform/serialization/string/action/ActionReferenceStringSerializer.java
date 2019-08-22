package dev.kkorolyov.pancake.platform.serialization.string.action;

import dev.kkorolyov.pancake.platform.Registry;
import dev.kkorolyov.pancake.platform.action.Action;

/**
 * Serializes action resources found in an {@link Action} {@link Registry}.
 */
public final class ActionReferenceStringSerializer extends ActionStringSerializer<Action> {
	// Module service provider
	public ActionReferenceStringSerializer() {
		this(null);
	}
	/**
	 * Constructs a new registered action serializer.
	 * @param context associated action registry
	 */
	public ActionReferenceStringSerializer(Registry<String, Action> context) {
		super("\\w+", context);
	}

	@Override
	public Action read(String out) {
		return context.get(out);
	}
	@Override
	public String write(Action in) {
		return context.getKeys(in).iterator().next();
	}
}
